/**
 * Copyright (C) 2006-2012 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.math.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.string.StringHelper;

/**
 * TestMatrix tests the functionality of the Jama Matrix class and associated
 * decompositions.
 * <P>
 * Run the test from the command line using <BLOCKQUOTE>
 * 
 * <PRE>
 * <CODE>
 *  java Jama.test.TestMatrix 
 * </CODE>
 * </PRE>
 * 
 * </BLOCKQUOTE> Detailed output is provided indicating the functionality being
 * tested and whether the functionality is correctly implemented. Exception
 * handling is also tested.
 * <P>
 * The test is designed to run to completion and give a summary of any
 * implementation errors encountered. The final output should be: <BLOCKQUOTE>
 * 
 * <PRE>
 * <CODE>
 *       TestMatrix completed.
 *       Total errors reported: n1
 *       Total warning reported: n2
 * </CODE>
 * </PRE>
 * 
 * </BLOCKQUOTE> If the test does not run to completion, this indicates that
 * there is a substantial problem within the implementation that was not
 * anticipated in the test design. The stopping point should give an indication
 * of where the problem exists.
 **/
public class TestMatrix
{
  private static final String FILENAME_JAMA_TEST_MATRIX_OUT = "Jamaout";
  private static final double EPSILON = Math.pow (2.0, -52.0);

  @Test
  public void testMain ()
  {
    Matrix A, B, C, Z, O, I, R, S, X, SUB, M, T, SQ, DEF, SOL;
    // Uncomment this to test IO in a different locale.
    // Locale.setDefault(Locale.GERMAN);
    int errorCount = 0;
    int warningCount = 0;
    double tmp;
    final double [] columnwise = { 1., 2., 3., 4., 5., 6., 7., 8., 9., 10., 11., 12. };
    final double [] rowwise = { 1., 4., 7., 10., 2., 5., 8., 11., 3., 6., 9., 12. };
    final double [][] avals = { { 1., 4., 7., 10. }, { 2., 5., 8., 11. }, { 3., 6., 9., 12. } };
    final double [][] rankdef = avals;
    final double [][] tvals = { { 1., 2., 3. }, { 4., 5., 6. }, { 7., 8., 9. }, { 10., 11., 12. } };
    final double [][] subavals = { { 5., 8., 11. }, { 6., 9., 12. } };
    final double [][] rvals = { { 1., 4., 7. }, { 2., 5., 8., 11. }, { 3., 6., 9., 12. } };
    final double [][] pvals = { { 4., 1., 1. }, { 1., 2., 3. }, { 1., 3., 6. } };
    final double [][] ivals = { { 1., 0., 0., 0. }, { 0., 1., 0., 0. }, { 0., 0., 1., 0. } };
    final double [][] evals = { { 0., 1., 0., 0. }, { 1., 0., 2.e-7, 0. }, { 0., -2.e-7, 0., 1. }, { 0., 0., 1., 0. } };
    final double [][] square = { { 166., 188., 210. }, { 188., 214., 240. }, { 210., 240., 270. } };
    final double [][] sqSolution = { { 13. }, { 15. } };
    final double [][] condmat = { { 1., 3. }, { 7., 9. } };
    final double [][] badeigs = { { 0, 0, 0, 0, 0 },
                                 { 0, 0, 0, 0, 1 },
                                 { 0, 0, 0, 1, 0 },
                                 { 1, 1, 0, 0, 1 },
                                 { 1, 0, 1, 0, 1 } };
    final int rows = 3, cols = 4;
    final int invalidld = 5;/*
                             * should trigger bad shape for construction with
                             * val
                             */
    final int raggedr = 0; /*
                            * (raggedr,raggedc) should be out of bounds in
                            * ragged array
                            */
    final int raggedc = 4;
    final int validld = 3; /* leading dimension of intended test Matrices */
    final int nonconformld = 4; /*
                                 * leading dimension which is valid, but
                                 * nonconforming
                                 */
    final int ib = 1, ie = 2, jb = 1, je = 3; /* index ranges for sub Matrix */
    final int [] rowindexset = { 1, 2 };
    final int [] badrowindexset = { 1, 3 };
    final int [] columnindexset = { 1, 2, 3 };
    final int [] badcolumnindexset = { 1, 2, 4 };
    final double columnsummax = 33.;
    final double rowsummax = 30.;
    final double sumofdiagonals = 15;
    final double sumofsquares = 650;

    /**
     * Constructors and constructor-like methods: double[], int double[][] int,
     * int int, int, double int, int, double[][] constructWithCopy(double[][])
     * random(int,int) identity(int)
     **/

    print ("\nTesting constructors and constructor-like methods...\n");
    try
    {
      /**
       * _check that exception is thrown in packed constructor with invalid
       * length
       **/
      A = new Matrix (columnwise, invalidld);
      errorCount = _try_failure (errorCount,
                                 "Catch invalid length in packed constructor... ",
                                 "exception not thrown for invalid input");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("Catch invalid length in packed constructor... ", e.getMessage ());
    }
    try
    {
      /**
       * _check that exception is thrown in default constructor if input array
       * is 'ragged'
       **/
      A = new Matrix (rvals);
      tmp = A.get (raggedr, raggedc);
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("Catch ragged input to default constructor... ", e.getMessage ());
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      errorCount = _try_failure (errorCount,
                                 "Catch ragged input to constructor... ",
                                 "exception not thrown in construction...ArrayIndexOutOfBoundsException thrown later");
    }
    try
    {
      /**
       * _check that exception is thrown in constructWithCopy if input array is
       * 'ragged'
       **/
      A = Matrix.constructWithCopy (rvals);
      tmp = A.get (raggedr, raggedc);
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("Catch ragged input to constructWithCopy... ", e.getMessage ());
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      errorCount = _try_failure (errorCount,
                                 "Catch ragged input to constructWithCopy... ",
                                 "exception not thrown in construction...ArrayIndexOutOfBoundsException thrown later");
    }

    A = new Matrix (columnwise, validld);
    B = new Matrix (avals);
    tmp = B.get (0, 0);
    avals[0][0] = 0.0;
    C = B.minus (A);
    avals[0][0] = tmp;
    B = Matrix.constructWithCopy (avals);
    tmp = B.get (0, 0);
    avals[0][0] = 0.0;
    if ((tmp - B.get (0, 0)) != 0.0)
    {
      /** _check that constructWithCopy behaves properly **/
      errorCount = _try_failure (errorCount, "constructWithCopy... ", "copy not effected... data visible outside");
    }
    else
    {
      _try_success ("constructWithCopy... ", "");
    }
    avals[0][0] = columnwise[0];
    I = new Matrix (ivals);
    try
    {
      _check (I, Matrix.identity (3, 4));
      _try_success ("identity... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "identity... ", "identity Matrix not successfully created");
    }

    /**
     * Access Methods: getColumnDimension() getRowDimension() getArray()
     * getArrayCopy() getColumnPackedCopy() getRowPackedCopy() get(int,int)
     * getMatrix(int,int,int,int) getMatrix(int,int,int[])
     * getMatrix(int[],int,int) getMatrix(int[],int[]) set(int,int,double)
     * setMatrix(int,int,int,int,Matrix) setMatrix(int,int,int[],Matrix)
     * setMatrix(int[],int,int,Matrix) setMatrix(int[],int[],Matrix)
     **/

    print ("\nTesting access methods...\n");

    /**
     * Various get methods:
     **/

    B = new Matrix (avals);
    if (B.getRowDimension () != rows)
    {
      errorCount = _try_failure (errorCount, "getRowDimension... ", "");
    }
    else
    {
      _try_success ("getRowDimension... ", "");
    }
    if (B.getColumnDimension () != cols)
    {
      errorCount = _try_failure (errorCount, "getColumnDimension... ", "");
    }
    else
    {
      _try_success ("getColumnDimension... ", "");
    }
    B = new Matrix (avals);
    double [][] barray = B.internalGetArray ();
    if (barray != avals)
    {
      errorCount = _try_failure (errorCount, "getArray... ", "");
    }
    else
    {
      _try_success ("getArray... ", "");
    }
    barray = B.getArrayCopy ();
    if (barray == avals)
    {
      errorCount = _try_failure (errorCount, "getArrayCopy... ", "data not (deep) copied");
    }
    try
    {
      _check (barray, avals);
      _try_success ("getArrayCopy... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "getArrayCopy... ", "data not successfully (deep) copied");
    }
    double [] bpacked = B.getColumnPackedCopy ();
    try
    {
      _check (bpacked, columnwise);
      _try_success ("getColumnPackedCopy... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount,
                                 "getColumnPackedCopy... ",
                                 "data not successfully (deep) copied by columns");
    }
    bpacked = B.getRowPackedCopy ();
    try
    {
      _check (bpacked, rowwise);
      _try_success ("getRowPackedCopy... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "getRowPackedCopy... ", "data not successfully (deep) copied by rows");
    }
    try
    {
      tmp = B.get (B.getRowDimension (), B.getColumnDimension () - 1);
      errorCount = _try_failure (errorCount, "get(int,int)... ", "OutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        tmp = B.get (B.getRowDimension () - 1, B.getColumnDimension ());
        errorCount = _try_failure (errorCount, "get(int,int)... ", "OutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("get(int,int)... OutofBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount, "get(int,int)... ", "OutOfBoundsException expected but not thrown");
    }
    try
    {
      if (B.get (B.getRowDimension () - 1, B.getColumnDimension () - 1) != avals[B.getRowDimension () - 1][B.getColumnDimension () - 1])
      {
        errorCount = _try_failure (errorCount, "get(int,int)... ", "Matrix entry (i,j) not successfully retreived");
      }
      else
      {
        _try_success ("get(int,int)... ", "");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      errorCount = _try_failure (errorCount, "get(int,int)... ", "Unexpected ArrayIndexOutOfBoundsException");
    }
    SUB = new Matrix (subavals);
    try
    {
      M = B.getMatrix (ib, ie + B.getRowDimension () + 1, jb, je);
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int,int,int,int)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        M = B.getMatrix (ib, ie, jb, je + B.getColumnDimension () + 1);
        errorCount = _try_failure (errorCount,
                                   "getMatrix(int,int,int,int)... ",
                                   "ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("getMatrix(int,int,int,int)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int,int,int,int)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      M = B.getMatrix (ib, ie, jb, je);
      try
      {
        _check (SUB, M);
        _try_success ("getMatrix(int,int,int,int)... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount, "getMatrix(int,int,int,int)... ", "submatrix not successfully retreived");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int,int,int,int)... ",
                                 "Unexpected ArrayIndexOutOfBoundsException");
    }

    try
    {
      M = B.getMatrix (ib, ie, badcolumnindexset);
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int,int,int[])... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        M = B.getMatrix (ib, ie + B.getRowDimension () + 1, columnindexset);
        errorCount = _try_failure (errorCount,
                                   "getMatrix(int,int,int[])... ",
                                   "ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("getMatrix(int,int,int[])... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int,int,int[])... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      M = B.getMatrix (ib, ie, columnindexset);
      try
      {
        _check (SUB, M);
        _try_success ("getMatrix(int,int,int[])... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount, "getMatrix(int,int,int[])... ", "submatrix not successfully retreived");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int,int,int[])... ",
                                 "Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      M = B.getMatrix (badrowindexset, jb, je);
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int[],int,int)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        M = B.getMatrix (rowindexset, jb, je + B.getColumnDimension () + 1);
        errorCount = _try_failure (errorCount,
                                   "getMatrix(int[],int,int)... ",
                                   "ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("getMatrix(int[],int,int)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int[],int,int)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      M = B.getMatrix (rowindexset, jb, je);
      try
      {
        _check (SUB, M);
        _try_success ("getMatrix(int[],int,int)... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount, "getMatrix(int[],int,int)... ", "submatrix not successfully retreived");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int[],int,int)... ",
                                 "Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      M = B.getMatrix (badrowindexset, columnindexset);
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int[],int[])... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        M = B.getMatrix (rowindexset, badcolumnindexset);
        errorCount = _try_failure (errorCount,
                                   "getMatrix(int[],int[])... ",
                                   "ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("getMatrix(int[],int[])... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "getMatrix(int[],int[])... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      M = B.getMatrix (rowindexset, columnindexset);
      try
      {
        _check (SUB, M);
        _try_success ("getMatrix(int[],int[])... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount, "getMatrix(int[],int[])... ", "submatrix not successfully retreived");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      errorCount = _try_failure (errorCount, "getMatrix(int[],int[])... ", "Unexpected ArrayIndexOutOfBoundsException");
    }

    /**
     * Various set methods:
     **/

    try
    {
      B.set (B.getRowDimension (), B.getColumnDimension () - 1, 0.);
      errorCount = _try_failure (errorCount, "set(int,int,double)... ", "OutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.set (B.getRowDimension () - 1, B.getColumnDimension (), 0.);
        errorCount = _try_failure (errorCount,
                                   "set(int,int,double)... ",
                                   "OutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("set(int,int,double)... OutofBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount, "set(int,int,double)... ", "OutOfBoundsException expected but not thrown");
    }
    try
    {
      B.set (ib, jb, 0.);
      tmp = B.get (ib, jb);
      try
      {
        _check (tmp, 0.);
        _try_success ("set(int,int,double)... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount, "set(int,int,double)... ", "Matrix element not successfully set");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      errorCount = _try_failure (errorCount, "set(int,int,double)... ", "Unexpected ArrayIndexOutOfBoundsException");
    }
    M = new Matrix (2, 3, 0.);
    try
    {
      B.setMatrix (ib, ie + B.getRowDimension () + 1, jb, je, M);
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int,int,int,int,Matrix)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.setMatrix (ib, ie, jb, je + B.getColumnDimension () + 1, M);
        errorCount = _try_failure (errorCount,
                                   "setMatrix(int,int,int,int,Matrix)... ",
                                   "ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("setMatrix(int,int,int,int,Matrix)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int,int,int,int,Matrix)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      B.setMatrix (ib, ie, jb, je, M);
      try
      {
        _check (M.minus (B.getMatrix (ib, ie, jb, je)), M);
        _try_success ("setMatrix(int,int,int,int,Matrix)... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount,
                                   "setMatrix(int,int,int,int,Matrix)... ",
                                   "submatrix not successfully set");
      }
      B.setMatrix (ib, ie, jb, je, SUB);
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int,int,int,int,Matrix)... ",
                                 "Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      B.setMatrix (ib, ie + B.getRowDimension () + 1, columnindexset, M);
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int,int,int[],Matrix)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.setMatrix (ib, ie, badcolumnindexset, M);
        errorCount = _try_failure (errorCount,
                                   "setMatrix(int,int,int[],Matrix)... ",
                                   "ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("setMatrix(int,int,int[],Matrix)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int,int,int[],Matrix)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      B.setMatrix (ib, ie, columnindexset, M);
      try
      {
        _check (M.minus (B.getMatrix (ib, ie, columnindexset)), M);
        _try_success ("setMatrix(int,int,int[],Matrix)... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount, "setMatrix(int,int,int[],Matrix)... ", "submatrix not successfully set");
      }
      B.setMatrix (ib, ie, jb, je, SUB);
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int,int,int[],Matrix)... ",
                                 "Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      B.setMatrix (rowindexset, jb, je + B.getColumnDimension () + 1, M);
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int[],int,int,Matrix)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.setMatrix (badrowindexset, jb, je, M);
        errorCount = _try_failure (errorCount,
                                   "setMatrix(int[],int,int,Matrix)... ",
                                   "ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("setMatrix(int[],int,int,Matrix)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int[],int,int,Matrix)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      B.setMatrix (rowindexset, jb, je, M);
      try
      {
        _check (M.minus (B.getMatrix (rowindexset, jb, je)), M);
        _try_success ("setMatrix(int[],int,int,Matrix)... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount, "setMatrix(int[],int,int,Matrix)... ", "submatrix not successfully set");
      }
      B.setMatrix (ib, ie, jb, je, SUB);
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int[],int,int,Matrix)... ",
                                 "Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      B.setMatrix (rowindexset, badcolumnindexset, M);
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int[],int[],Matrix)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.setMatrix (badrowindexset, columnindexset, M);
        errorCount = _try_failure (errorCount,
                                   "setMatrix(int[],int[],Matrix)... ",
                                   "ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("setMatrix(int[],int[],Matrix)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int[],int[],Matrix)... ",
                                 "ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      B.setMatrix (rowindexset, columnindexset, M);
      try
      {
        _check (M.minus (B.getMatrix (rowindexset, columnindexset)), M);
        _try_success ("setMatrix(int[],int[],Matrix)... ", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount, "setMatrix(int[],int[],Matrix)... ", "submatrix not successfully set");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      errorCount = _try_failure (errorCount,
                                 "setMatrix(int[],int[],Matrix)... ",
                                 "Unexpected ArrayIndexOutOfBoundsException");
    }

    /**
     * Array-like methods: minus minusEquals plus plusEquals arrayLeftDivide
     * arrayLeftDivideEquals arrayRightDivide arrayRightDivideEquals arrayTimes
     * arrayTimesEquals uminus
     **/

    print ("\nTesting array-like methods...\n");
    S = new Matrix (columnwise, nonconformld);
    R = Matrix.random (A.getRowDimension (), A.getColumnDimension ());
    A = R;
    try
    {
      S = A.minus (S);
      errorCount = _try_failure (errorCount, "minus conformance _check... ", "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("minus conformance _check... ", "");
    }
    if (A.minus (R).norm1 () != 0.)
    {
      errorCount = _try_failure (errorCount,
                                 "minus... ",
                                 "(difference of identical Matrices is nonzero,\nSubsequent use of minus should be suspect)");
    }
    else
    {
      _try_success ("minus... ", "");
    }
    A = R.getClone ();
    A.minusEquals (R);
    Z = new Matrix (A.getRowDimension (), A.getColumnDimension ());
    try
    {
      A.minusEquals (S);
      errorCount = _try_failure (errorCount, "minusEquals conformance _check... ", "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("minusEquals conformance _check... ", "");
    }
    if (A.minus (Z).norm1 () != 0.)
    {
      errorCount = _try_failure (errorCount,
                                 "minusEquals... ",
                                 "(difference of identical Matrices is nonzero,\nSubsequent use of minus should be suspect)");
    }
    else
    {
      _try_success ("minusEquals... ", "");
    }

    A = R.getClone ();
    B = Matrix.random (A.getRowDimension (), A.getColumnDimension ());
    C = A.minus (B);
    try
    {
      S = A.plus (S);
      errorCount = _try_failure (errorCount, "plus conformance _check... ", "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("plus conformance _check... ", "");
    }
    try
    {
      _check (C.plus (B), A);
      _try_success ("plus... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "plus... ", "(C = A - B, but C + B != A)");
    }
    C = A.minus (B);
    C.plusEquals (B);
    try
    {
      A.plusEquals (S);
      errorCount = _try_failure (errorCount, "plusEquals conformance _check... ", "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("plusEquals conformance _check... ", "");
    }
    try
    {
      _check (C, A);
      _try_success ("plusEquals... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "plusEquals... ", "(C = A - B, but C = C + B != A)");
    }
    A = R.uminus ();
    try
    {
      _check (A.plus (R), Z);
      _try_success ("uminus... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "uminus... ", "(-A + A != zeros)");
    }
    A = R.getClone ();
    O = new Matrix (A.getRowDimension (), A.getColumnDimension (), 1.0);
    C = A.arrayLeftDivide (R);
    try
    {
      S = A.arrayLeftDivide (S);
      errorCount = _try_failure (errorCount, "arrayLeftDivide conformance _check... ", "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("arrayLeftDivide conformance _check... ", "");
    }
    try
    {
      _check (C, O);
      _try_success ("arrayLeftDivide... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "arrayLeftDivide... ", "(M.\\M != ones)");
    }
    try
    {
      A.arrayLeftDivideEquals (S);
      errorCount = _try_failure (errorCount,
                                 "arrayLeftDivideEquals conformance _check... ",
                                 "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("arrayLeftDivideEquals conformance _check... ", "");
    }
    A.arrayLeftDivideEquals (R);
    try
    {
      _check (A, O);
      _try_success ("arrayLeftDivideEquals... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "arrayLeftDivideEquals... ", "(M.\\M != ones)");
    }
    A = R.getClone ();
    try
    {
      A.arrayRightDivide (S);
      errorCount = _try_failure (errorCount, "arrayRightDivide conformance _check... ", "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("arrayRightDivide conformance _check... ", "");
    }
    C = A.arrayRightDivide (R);
    try
    {
      _check (C, O);
      _try_success ("arrayRightDivide... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "arrayRightDivide... ", "(M./M != ones)");
    }
    try
    {
      A.arrayRightDivideEquals (S);
      errorCount = _try_failure (errorCount,
                                 "arrayRightDivideEquals conformance _check... ",
                                 "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("arrayRightDivideEquals conformance _check... ", "");
    }
    A.arrayRightDivideEquals (R);
    try
    {
      _check (A, O);
      _try_success ("arrayRightDivideEquals... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "arrayRightDivideEquals... ", "(M./M != ones)");
    }
    A = R.getClone ();
    B = Matrix.random (A.getRowDimension (), A.getColumnDimension ());
    try
    {
      S = A.arrayTimes (S);
      errorCount = _try_failure (errorCount, "arrayTimes conformance _check... ", "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("arrayTimes conformance _check... ", "");
    }
    C = A.arrayTimes (B);
    try
    {
      _check (C.arrayRightDivideEquals (B), A);
      _try_success ("arrayTimes... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "arrayTimes... ", "(A = R, C = A.*B, but C./B != A)");
    }
    try
    {
      A.arrayTimesEquals (S);
      errorCount = _try_failure (errorCount, "arrayTimesEquals conformance _check... ", "nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("arrayTimesEquals conformance _check... ", "");
    }
    A.arrayTimesEquals (B);
    try
    {
      _check (A.arrayRightDivideEquals (B), R);
      _try_success ("arrayTimesEquals... ", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "arrayTimesEquals... ", "(A = R, A = A.*B, but A./B != R)");
    }

    /**
     * I/O methods: read print serializable: writeObject readObject
     **/
    print ("\nTesting I/O methods...\n");
    try
    {
      final DecimalFormat fmt = new DecimalFormat ("0.0000E00");
      fmt.setDecimalFormatSymbols (new DecimalFormatSymbols (Locale.US));

      final PrintWriter aPW = new PrintWriter (new FileOutputStream (FILENAME_JAMA_TEST_MATRIX_OUT));
      A.print (aPW, fmt, 10);
      aPW.close ();
      final BufferedReader aReader = new BufferedReader (new FileReader (FILENAME_JAMA_TEST_MATRIX_OUT));
      R = Matrix.read (aReader);
      aReader.close ();
      if (A.minus (R).norm1 () < .001)
      {
        _try_success ("print()/read()...", "");
      }
      else
      {
        errorCount = _try_failure (errorCount,
                                   "print()/read()...",
                                   "Matrix read from file does not match Matrix printed to file");
      }
    }
    catch (final IOException ioe)
    {
      warningCount = _try_warning (warningCount,
                                   "print()/read()...",
                                   "unexpected I/O error, unable to run print/read test;  _check write permission in current directory and retry");
    }
    catch (final Exception e)
    {
      try
      {
        e.printStackTrace (System.out);
        warningCount = _try_warning (warningCount,
                                     "print()/read()...",
                                     "Formatting error... will try JDK1.1 reformulation...");
        final DecimalFormat fmt = new DecimalFormat ("0.0000");
        final PrintWriter FILE = new PrintWriter (new FileOutputStream (FILENAME_JAMA_TEST_MATRIX_OUT));
        A.print (FILE, fmt, 10);
        FILE.close ();
        final BufferedReader aReader = new BufferedReader (new FileReader (FILENAME_JAMA_TEST_MATRIX_OUT));
        R = Matrix.read (aReader);
        aReader.close ();
        if (A.minus (R).norm1 () < .001)
        {
          _try_success ("print()/read()...", "");
        }
        else
        {
          errorCount = _try_failure (errorCount,
                                     "print()/read() (2nd attempt) ...",
                                     "Matrix read from file does not match Matrix printed to file");
        }
      }
      catch (final IOException ioe)
      {
        warningCount = _try_warning (warningCount,
                                     "print()/read()...",
                                     "unexpected I/O error, unable to run print/read test;  _check write permission in current directory and retry");
      }
    }
    finally
    {
      FileOperations.deleteFile (new File (FILENAME_JAMA_TEST_MATRIX_OUT));
    }

    R = Matrix.random (A.getRowDimension (), A.getColumnDimension ());
    final String tmpname = "TMPMATRIX.serial";
    try
    {
      final ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream (tmpname));
      out.writeObject (R);
      out.close ();
      final ObjectInputStream sin = new ObjectInputStream (new FileInputStream (tmpname));
      A = (Matrix) sin.readObject ();
      sin.close ();

      try
      {
        _check (A, R);
        _try_success ("writeObject(Matrix)/readObject(Matrix)...", "");
      }
      catch (final RuntimeException e)
      {
        errorCount = _try_failure (errorCount,
                                   "writeObject(Matrix)/readObject(Matrix)...",
                                   "Matrix not serialized correctly");
      }
    }
    catch (final IOException ioe)
    {
      warningCount = _try_warning (warningCount,
                                   "writeObject()/readObject()...",
                                   "unexpected I/O error, unable to run serialization test;  _check write permission in current directory and retry");
    }
    catch (final Exception e)
    {
      errorCount = _try_failure (errorCount,
                                 "writeObject(Matrix)/readObject(Matrix)...",
                                 "unexpected error in serialization test");
    }
    finally
    {
      FileOperations.deleteFile (new File (tmpname));
    }

    /**
     * LA methods: transpose times cond rank det trace norm1 norm2 normF normInf
     * solve solveTranspose inverse chol eig lu qr svd
     **/

    print ("\nTesting linear algebra methods...\n");
    A = new Matrix (columnwise, 3);
    T = new Matrix (tvals);
    T = A.transpose ();
    try
    {
      _check (A.transpose (), T);
      _try_success ("transpose...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "transpose()...", "transpose unsuccessful");
    }
    A.transpose ();
    try
    {
      _check (A.norm1 (), columnsummax);
      _try_success ("norm1...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "norm1()...", "incorrect norm calculation");
    }
    try
    {
      _check (A.normInf (), rowsummax);
      _try_success ("normInf()...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "normInf()...", "incorrect norm calculation");
    }
    try
    {
      _check (A.normF (), Math.sqrt (sumofsquares));
      _try_success ("normF...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "normF()...", "incorrect norm calculation");
    }
    try
    {
      _check (A.trace (), sumofdiagonals);
      _try_success ("trace()...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "trace()...", "incorrect trace calculation");
    }
    try
    {
      _check (A.getMatrix (0, A.getRowDimension () - 1, 0, A.getRowDimension () - 1).det (), 0.);
      _try_success ("det()...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "det()...", "incorrect determinant calculation");
    }
    SQ = new Matrix (square);
    try
    {
      _check (A.times (A.transpose ()), SQ);
      _try_success ("times(Matrix)...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "times(Matrix)...", "incorrect Matrix-Matrix product calculation");
    }
    try
    {
      _check (A.times (0.), Z);
      _try_success ("times(double)...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "times(double)...", "incorrect Matrix-scalar product calculation");
    }

    A = new Matrix (columnwise, 4);
    final QRDecomposition QR = A.qr ();
    R = QR.getR ();
    try
    {
      _check (A, QR.getQ ().times (R));
      _try_success ("QRDecomposition...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "QRDecomposition...", "incorrect QR decomposition calculation");
    }
    SingularValueDecomposition SVD = A.svd ();
    try
    {
      _check (A, SVD.getU ().times (SVD.getS ().times (SVD.getV ().transpose ())));
      _try_success ("SingularValueDecomposition...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount,
                                 "SingularValueDecomposition...",
                                 "incorrect singular value decomposition calculation");
    }
    DEF = new Matrix (rankdef);
    try
    {
      _check (DEF.rank (), Math.min (DEF.getRowDimension (), DEF.getColumnDimension ()) - 1);
      _try_success ("rank()...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "rank()...", "incorrect rank calculation");
    }
    B = new Matrix (condmat);
    SVD = B.svd ();
    final double [] singularvalues = SVD.getSingularValues ();
    try
    {
      _check (B.cond (), singularvalues[0] /
                         singularvalues[Math.min (B.getRowDimension (), B.getColumnDimension ()) - 1]);
      _try_success ("cond()...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "cond()...", "incorrect condition number calculation");
    }
    final int n = A.getColumnDimension ();
    A = A.getMatrix (0, n - 1, 0, n - 1);
    A.set (0, 0, 0.);
    final LUDecomposition LU = A.lu ();
    try
    {
      _check (A.getMatrix (LU.getPivot (), 0, n - 1), LU.getL ().times (LU.getU ()));
      _try_success ("LUDecomposition...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "LUDecomposition...", "incorrect LU decomposition calculation");
    }
    X = A.inverse ();
    try
    {
      _check (A.times (X), Matrix.identity (3, 3));
      _try_success ("inverse()...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "inverse()...", "incorrect inverse calculation");
    }
    O = new Matrix (SUB.getRowDimension (), 1, 1.0);
    SOL = new Matrix (sqSolution);
    SQ = SUB.getMatrix (0, SUB.getRowDimension () - 1, 0, SUB.getRowDimension () - 1);
    try
    {
      _check (SQ.solve (SOL), O);
      _try_success ("solve()...", "");
    }
    catch (final IllegalArgumentException e1)
    {
      errorCount = _try_failure (errorCount, "solve()...", e1.getMessage ());
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "solve()...", e.getMessage ());
    }
    A = new Matrix (pvals);
    final CholeskyDecomposition Chol = A.chol ();
    final Matrix L = Chol.getL ();
    try
    {
      _check (A, L.times (L.transpose ()));
      _try_success ("CholeskyDecomposition...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "CholeskyDecomposition...", "incorrect Cholesky decomposition calculation");
    }
    X = Chol.solve (Matrix.identity (3, 3));
    try
    {
      _check (A.times (X), Matrix.identity (3, 3));
      _try_success ("CholeskyDecomposition solve()...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount,
                                 "CholeskyDecomposition solve()...",
                                 "incorrect Choleskydecomposition solve calculation");
    }
    EigenvalueDecomposition Eig = A.eig ();
    Matrix D = Eig.getD ();
    Matrix V = Eig.getV ();
    try
    {
      _check (A.times (V), V.times (D));
      _try_success ("EigenvalueDecomposition (symmetric)...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount,
                                 "EigenvalueDecomposition (symmetric)...",
                                 "incorrect symmetric Eigenvalue decomposition calculation");
    }
    A = new Matrix (evals);
    Eig = A.eig ();
    D = Eig.getD ();
    V = Eig.getV ();
    try
    {
      _check (A.times (V), V.times (D));
      _try_success ("EigenvalueDecomposition (nonsymmetric)...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount,
                                 "EigenvalueDecomposition (nonsymmetric)...",
                                 "incorrect nonsymmetric Eigenvalue decomposition calculation");
    }

    try
    {
      print ("\nTesting Eigenvalue; If this hangs, we've failed\n");
      final Matrix bA = new Matrix (badeigs);
      final EigenvalueDecomposition bEig = bA.eig ();
      assertNotNull (bEig);
      _try_success ("EigenvalueDecomposition (hang)...", "");
    }
    catch (final RuntimeException e)
    {
      errorCount = _try_failure (errorCount, "EigenvalueDecomposition (hang)...", "incorrect termination");
    }

    print ("\nTestMatrix completed.\n");
    print ("Total errors reported: " + errorCount + "\n");
    print ("Total warnings reported: " + warningCount + "\n");

    assertEquals (0, errorCount);
    assertEquals (0, warningCount);
  }

  /** private utility routines **/

  /** Check magnitude of difference of scalars. **/

  private static void _check (final double x, final double y)
  {
    if (x == 0 && Math.abs (y) < 10 * EPSILON)
      return;
    if (y == 0 && Math.abs (x) < 10 * EPSILON)
      return;
    if (Math.abs (x - y) > 10 * EPSILON * Math.max (Math.abs (x), Math.abs (y)))
    {
      throw new RuntimeException ("The difference x-y is too large: x = " +
                                  Double.toString (x) +
                                  "  y = " +
                                  Double.toString (y));
    }
  }

  /** Check norm of difference of "vectors". **/

  private static void _check (final double [] x, final double [] y)
  {
    if (x.length == y.length)
    {
      for (int i = 0; i < x.length; i++)
      {
        _check (x[i], y[i]);
      }
    }
    else
    {
      throw new RuntimeException ("Attempt to compare vectors of different lengths");
    }
  }

  /** Check norm of difference of arrays. **/

  private static void _check (final double [][] x, final double [][] y)
  {
    final Matrix A = new Matrix (x);
    final Matrix B = new Matrix (y);
    _check (A, B);
  }

  /** Check norm of difference of Matrices. **/

  private static void _check (@Nonnull final Matrix X, @Nonnull final Matrix Y)
  {
    if (X.norm1 () == 0. && Y.norm1 () < 10 * EPSILON)
      return;
    if (Y.norm1 () == 0. && X.norm1 () < 10 * EPSILON)
      return;
    if (X.minus (Y).norm1 () > 1000 * EPSILON * Math.max (X.norm1 (), Y.norm1 ()))
      throw new RuntimeException ("The norm of (X-Y) is too large: " + Double.toString (X.minus (Y).norm1 ()));
  }

  /** Shorten spelling of print. **/

  private static void print (final String s)
  {
    System.out.print (s);
  }

  /** Print appropriate messages for successful outcome try **/

  private static void _try_success (final String s, final String e)
  {
    print (">    " + s + "success\n");
    if (StringHelper.hasText (e))
      print (">      Message: " + e + "\n");
  }

  /** Print appropriate messages for unsuccessful outcome try **/

  private static int _try_failure (final int count, final String s, final String e)
  {
    print (">    " + s + "*** failure ***\n>      Message: " + e + "\n");
    return count + 1;
  }

  /** Print appropriate messages for unsuccessful outcome try **/

  private static int _try_warning (final int count, final String s, final String e)
  {
    print (">    " + s + "*** warning ***\n>      Message: " + e + "\n");
    return count + 1;
  }

  /** Print a row vector. **/

  @SuppressWarnings ("unused")
  private static void _print (final double [] x, final int w, final int d)
  {
    // Use format Fw.d for all elements.
    System.out.print ("\n");
    new Matrix (x, 1).print (w, d);
    print ("\n");
  }
}
