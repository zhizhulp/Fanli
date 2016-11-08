/* Generated by Together */

package com.ascba.fanli.view.cityList;

/** A '.' node test.
 * This is part of the GoF Flyweight(195) pattern -- Only one object of
 * this class ever exists, shared amongst all clients.
 * You use INSTANCE instead of the constructor to get
 * that object.

   <blockquote><small> Copyright (C) 2002 Hewlett-Packard Company.
   This file is part of Sparta, an XML Parser, DOM, and XPath library.
   This library is free software; you can redistribute it and/or
   modify it under the terms of the <a href="doc-files/LGPL.txt">GNU
   Lesser General Public License</a> as published by the Free Software
   Foundation; either version 2.1 of the License, or (at your option)
   any later version.  This library is distributed in the hope that it
   will be useful, but WITHOUT ANY WARRANTY; without even the implied
   warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
   PURPOSE. </small></blockquote>
   @version  $Date: 2002/12/06 23:41:18 $  $Revision: 1.2 $
   @author Eamonn O'Brien-Strain
*/
public class ThisNodeTest extends NodeTest {

    //only need one of them => GoF Flyweight Pattern(195) 
    private ThisNodeTest(){}   
    static final ThisNodeTest INSTANCE = new ThisNodeTest(); 

    public void accept(Visitor visitor){
        visitor.visit(this);
    }

    /** Return false*/
    public boolean isStringValue(){
        return false;
    }

    public String toString(){
        return ".";
    }
}

// $Log: ThisNodeTest.java,v $
// Revision 1.2  2002/12/06 23:41:18  eobrain
// Make objects that are always the same follow the Flyweight Pattern.
//
// Revision 1.1.1.1  2002/08/19 05:04:03  eobrain
// import from HP Labs internal CVS
//
// Revision 1.3  2002/08/18 23:39:10  eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.2  2002/06/14 19:41:56  eob
// Make test for isStringValue more object-oriented.  Avoid "instanceof".
//
// Revision 1.1  2002/02/01 02:50:18  eob
// initial
