/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */


package org.graalvm.compiler.hotspot.test;

import java.util.function.Function;

import org.graalvm.compiler.test.GraalTest;
import org.junit.Assume;
import org.junit.Test;

import jdk.vm.ci.code.InstalledCode;
import jdk.vm.ci.code.InvalidInstalledCodeException;
import jdk.vm.ci.code.stack.InspectedFrame;
import jdk.vm.ci.code.stack.StackIntrospection;
import jdk.vm.ci.hotspot.HotSpotJVMCIRuntime;
import jdk.vm.ci.meta.ResolvedJavaMethod;

/**
 * Create a single object which is referenced from a local, the expression stack and the lock state
 * and then ensure that identity is maintained when the frame is forced to be materialized by
 * {@link InspectedFrame#materializeVirtualObjects(boolean)}.
 */
public class HotSpotStackIntrospectionTest extends HotSpotGraalCompilerTest {

    static StackIntrospection stackIntrospection = HotSpotJVMCIRuntime.runtime().getHostJVMCIBackend().getStackIntrospection();
    static volatile int v;

    public static void testSynchronizedSnippet(Function<Void, Void> f) {
        Object a = new Object();
        synchronized (a) {
            testOnStack(a, forceFrameState(a, f), a);
            // This object should be locked so try to notify on it
            a.notify();
        }
    }

    public static void testSnippet(Function<Void, Void> f) {
        Object a = new Object();
        testOnStack(a, forceFrameState(a, f), a);
    }

    private static void testOnStack(Object a, Object a2, Object a3) {
        if (a != a2 || a != a3) {
            throw new InternalError();
        }
    }

    private static Object forceFrameState(Object a, Function<Void, Void> f) {
        // Use a volatile store to ensure a FrameState is captured after this point.
        v++;
        f.apply(null);
        return a;
    }

    @Test(timeout = 20000)
    public void run() throws InvalidInstalledCodeException {
        // The JDK9 bits are currently broken
        Assume.assumeTrue(GraalTest.Java8OrEarlier);
        test("testSnippet");
    }

    @Test(timeout = 20000)
    public void runSynchronized() throws InvalidInstalledCodeException {
        // The JDK9 bits are currently broken
        Assume.assumeTrue(GraalTest.Java8OrEarlier);
        test("testSynchronizedSnippet");
    }

    private void test(String name) throws InvalidInstalledCodeException {
        ResolvedJavaMethod method = getMetaAccess().lookupJavaMethod(getMethod(name));
        Function<Void, Void> f = o -> {
            stackIntrospection.iterateFrames(null, null, 0, frame -> {
                if (frame.getMethod().equals(method)) {
                    frame.materializeVirtualObjects(true);
                }
                return null;
            });
            return null;
        };
        InstalledCode code = getCode(method);
        code.executeVarargs(f);
    }

}
