package org.omg.CosNaming.NamingContextPackage;


/**
* org/omg/CosNaming/NamingContextPackage/AlreadyBoundHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../../../../src/share/classes/org/omg/CosNaming/nameservice.idl
* Tuesday, March 26, 2013 2:15:02 PM GMT-08:00
*/

abstract public class AlreadyBoundHelper
{
  private static String  _id = "IDL:omg.org/CosNaming/NamingContext/AlreadyBound:1.0";

  public static void insert (org.omg.CORBA.Any a, org.omg.CosNaming.NamingContextPackage.AlreadyBound that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static org.omg.CosNaming.NamingContextPackage.AlreadyBound extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [0];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          __typeCode = org.omg.CORBA.ORB.init ().create_exception_tc (org.omg.CosNaming.NamingContextPackage.AlreadyBoundHelper.id (), "AlreadyBound", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static org.omg.CosNaming.NamingContextPackage.AlreadyBound read (org.omg.CORBA.portable.InputStream istream)
  {
    org.omg.CosNaming.NamingContextPackage.AlreadyBound value = new org.omg.CosNaming.NamingContextPackage.AlreadyBound ();
    // read and discard the repository ID
    istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, org.omg.CosNaming.NamingContextPackage.AlreadyBound value)
  {
    // write the repository ID
    ostream.write_string (id ());
  }

}