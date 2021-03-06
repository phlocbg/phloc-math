/**
 * Copyright (C) 2006-2014 phloc systems
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
import static org.junit.Assert.fail;

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
import java.util.Locale;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.commons.io.file.FileOperations;
import com.phloc.commons.lang.DecimalFormatSymbolsFactory;
import com.phloc.commons.string.StringHelper;

/**
 * TestMatrix tests the functionality of the Jama MatrixInt class and associated
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
public class MatrixIntTest
{
  private static final String FILENAME_JAMA_TEST_MATRIX_OUT = "Jamaout";
  private static final double EPSILON = Math.pow (2.0, -52.0);

  @Test
  public void testMain ()
  {
    // Uncomment this to test IO in a different locale.
    // Locale.setDefault(Locale.GERMAN);
    int warningCount = 0;
    int tmp;
    final int [] columnwise = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    final int [] rowwise = { 1, 4, 7, 10, 2, 5, 8, 11, 3, 6, 9, 12 };
    final int [][] avals = { { 1, 4, 7, 10 }, { 2, 5, 8, 11 }, { 3, 6, 9, 12 } };
    final int [][] tvals = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
    final int [][] subavals = { { 5, 8, 11 }, { 6, 9, 12 } };
    final int [][] rvals = { { 1, 4, 7 }, { 2, 5, 8, 11 }, { 3, 6, 9, 12 } };
    final int [][] ivals = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 } };
    final int [][] square = { { 166, 188, 210 }, { 188, 214, 240 }, { 210, 240, 270 } };
    final int rows = 3, cols = 4;
    /*
     * should trigger bad shape for construction with val
     */
    final int invalidld = 5;
    /*
     * (raggedr,raggedc) should be out of bounds in ragged array
     */
    final int raggedr = 0;
    final int raggedc = 4;
    /* leading dimension of intended test Matrices */
    final int validld = 3;
    /*
     * leading dimension which is valid, but nonconforming
     */
    final int nonconformld = 4;
    /* index ranges for sub MatrixInt */
    final int ib = 1, ie = 2, jb = 1, je = 3;
    final int [] rowindexset = { 1, 2 };
    final int [] badrowindexset = { 1, 3 };
    final int [] columnindexset = { 1, 2, 3 };
    final int [] badcolumnindexset = { 1, 2, 4 };
    final int columnsummax = 33;
    final int rowsummax = 30;
    final int sumofdiagonals = 15;
    final int sumofsquares = 650;

    /**
     * Constructors and constructor-like methods: double[], int double[][] int,
     * int int, int, double int, int, double[][] constructWithCopy(double[][])
     * random(int,int) identity(int)
     **/

    _print ("\nTesting constructors and constructor-like methods...\n");
    try
    {
      /**
       * _check that exception is thrown in packed constructor with invalid
       * length
       **/
      new MatrixInt (columnwise, invalidld);
      fail ("exception not thrown for invalid input");
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
      final MatrixInt A = new MatrixInt (rvals);
      tmp = A.get (raggedr, raggedc);
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("Catch ragged input to default constructor... ", e.getMessage ());
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      fail ("exception not thrown in construction...ArrayIndexOutOfBoundsException thrown later");
    }
    try
    {
      /**
       * _check that exception is thrown in constructWithCopy if input array is
       * 'ragged'
       **/
      final MatrixInt A = MatrixInt.constructWithCopy (rvals);
      tmp = A.get (raggedr, raggedc);
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("Catch ragged input to constructWithCopy... ", e.getMessage ());
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      fail ("exception not thrown in construction...ArrayIndexOutOfBoundsException thrown later");
    }

    MatrixInt A = new MatrixInt (columnwise, validld);
    MatrixInt B = new MatrixInt (avals);
    tmp = B.get (0, 0);
    avals[0][0] = 0;
    MatrixInt C = B.minus (A);
    avals[0][0] = tmp;
    B = MatrixInt.constructWithCopy (avals);
    tmp = B.get (0, 0);
    avals[0][0] = 0;
    if ((tmp - B.get (0, 0)) != 0.0)
    {
      /** _check that constructWithCopy behaves properly **/
      fail ("copy not effected... data visible outside");
    }
    else
    {
      _try_success ("constructWithCopy... ", "");
    }
    avals[0][0] = columnwise[0];
    final MatrixInt I = new MatrixInt (ivals);
    try
    {
      _check (I, MatrixInt.identity (3, 4));
      _try_success ("identity... ", "");
    }
    catch (final RuntimeException e)
    {
      fail ("identity MatrixInt not successfully created");
    }

    /**
     * Access Methods: getColumnDimension() getRowDimension() getArray()
     * getArrayCopy() getColumnPackedCopy() getRowPackedCopy() get(int,int)
     * getMatrix(int,int,int,int) getMatrix(int,int,int[])
     * getMatrix(int[],int,int) getMatrix(int[],int[]) set(int,int,double)
     * setMatrix(int,int,int,int,MatrixInt) setMatrix(int,int,int[],MatrixInt)
     * setMatrix(int[],int,int,MatrixInt) setMatrix(int[],int[],MatrixInt)
     **/

    _print ("\nTesting access methods...\n");

    /**
     * Various get methods:
     **/

    B = new MatrixInt (avals);
    if (B.getRowDimension () != rows)
    {
      fail ();
    }
    else
    {
      _try_success ("getRowDimension... ", "");
    }
    if (B.getColumnDimension () != cols)
    {
      fail ();
    }
    else
    {
      _try_success ("getColumnDimension... ", "");
    }
    B = new MatrixInt (avals);
    int [][] barray = B.internalGetArray ();
    if (barray != avals)
    {
      fail ();
    }
    else
    {
      _try_success ("getArray... ", "");
    }
    barray = B.getArrayCopy ();
    if (barray == avals)
    {
      fail ("data not (deep) copied");
    }
    try
    {
      _check (barray, avals);
      _try_success ("getArrayCopy... ", "");
    }
    catch (final RuntimeException e)
    {
      fail ("data not successfully (deep) copied");
    }
    int [] bpacked = B.getColumnPackedCopy ();
    try
    {
      _check (bpacked, columnwise);
      _try_success ("getColumnPackedCopy... ", "");
    }
    catch (final RuntimeException e)
    {
      fail ("data not successfully (deep) copied by columns");
    }
    bpacked = B.getRowPackedCopy ();
    try
    {
      _check (bpacked, rowwise);
      _try_success ("getRowPackedCopy... ", "");
    }
    catch (final RuntimeException e)
    {
      fail ("data not successfully (deep) copied by rows");
    }
    try
    {
      tmp = B.get (B.getRowDimension (), B.getColumnDimension () - 1);
      fail ("OutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        tmp = B.get (B.getRowDimension () - 1, B.getColumnDimension ());
        fail ("OutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("get(int,int)... OutofBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("OutOfBoundsException expected but not thrown");
    }
    try
    {
      if (B.get (B.getRowDimension () - 1, B.getColumnDimension () - 1) != avals[B.getRowDimension () - 1][B.getColumnDimension () - 1])
      {
        fail ("MatrixInt entry (i,j) not successfully retreived");
      }
      else
      {
        _try_success ("get(int,int)... ", "");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }
    final MatrixInt SUB = new MatrixInt (subavals);
    MatrixInt M;
    try
    {
      M = B.getMatrix (ib, ie + B.getRowDimension () + 1, jb, je);
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        M = B.getMatrix (ib, ie, jb, je + B.getColumnDimension () + 1);
        fail ("ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("getMatrix(int,int,int,int)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
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
        fail ("submatrix not successfully retreived");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }

    try
    {
      M = B.getMatrix (ib, ie, badcolumnindexset);
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        M = B.getMatrix (ib, ie + B.getRowDimension () + 1, columnindexset);
        fail ("ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("getMatrix(int,int,int[])... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
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
        fail ("submatrix not successfully retreived");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      M = B.getMatrix (badrowindexset, jb, je);
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        M = B.getMatrix (rowindexset, jb, je + B.getColumnDimension () + 1);
        fail ("ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("getMatrix(int[],int,int)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
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
        fail ("submatrix not successfully retreived");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      M = B.getMatrix (badrowindexset, columnindexset);
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        M = B.getMatrix (rowindexset, badcolumnindexset);
        fail ("ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("getMatrix(int[],int[])... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
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
        fail ("submatrix not successfully retreived");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }

    /**
     * Various set methods:
     **/

    try
    {
      B.set (B.getRowDimension (), B.getColumnDimension () - 1, 0);
      fail ("OutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.set (B.getRowDimension () - 1, B.getColumnDimension (), 0);
        fail ("OutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("set(int,int,double)... OutofBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("OutOfBoundsException expected but not thrown");
    }
    try
    {
      B.set (ib, jb, 0);
      tmp = B.get (ib, jb);
      try
      {
        _check (tmp, 0);
        _try_success ("set(int,int,double)... ", "");
      }
      catch (final RuntimeException e)
      {
        fail ("MatrixInt element not successfully set");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }
    M = new MatrixInt (2, 3, 0);
    try
    {
      B.setMatrix (ib, ie + B.getRowDimension () + 1, jb, je, M);
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.setMatrix (ib, ie, jb, je + B.getColumnDimension () + 1, M);
        fail ("ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("setMatrix(int,int,int,int,MatrixInt)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      B.setMatrix (ib, ie, jb, je, M);
      try
      {
        _check (M.minus (B.getMatrix (ib, ie, jb, je)), M);
        _try_success ("setMatrix(int,int,int,int,MatrixInt)... ", "");
      }
      catch (final RuntimeException e)
      {
        fail ("submatrix not successfully set");
      }
      B.setMatrix (ib, ie, jb, je, SUB);
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      B.setMatrix (ib, ie + B.getRowDimension () + 1, columnindexset, M);
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.setMatrix (ib, ie, badcolumnindexset, M);
        fail ("ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("setMatrix(int,int,int[],MatrixInt)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      B.setMatrix (ib, ie, columnindexset, M);
      try
      {
        _check (M.minus (B.getMatrix (ib, ie, columnindexset)), M);
        _try_success ("setMatrix(int,int,int[],MatrixInt)... ", "");
      }
      catch (final RuntimeException e)
      {
        fail ("submatrix not successfully set");
      }
      B.setMatrix (ib, ie, jb, je, SUB);
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      B.setMatrix (rowindexset, jb, je + B.getColumnDimension () + 1, M);
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.setMatrix (badrowindexset, jb, je, M);
        fail ("ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("setMatrix(int[],int,int,MatrixInt)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      B.setMatrix (rowindexset, jb, je, M);
      try
      {
        _check (M.minus (B.getMatrix (rowindexset, jb, je)), M);
        _try_success ("setMatrix(int[],int,int,MatrixInt)... ", "");
      }
      catch (final RuntimeException e)
      {
        fail ("submatrix not successfully set");
      }
      B.setMatrix (ib, ie, jb, je, SUB);
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }
    try
    {
      B.setMatrix (rowindexset, badcolumnindexset, M);
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    catch (final ArrayIndexOutOfBoundsException e)
    {
      try
      {
        B.setMatrix (badrowindexset, columnindexset, M);
        fail ("ArrayIndexOutOfBoundsException expected but not thrown");
      }
      catch (final ArrayIndexOutOfBoundsException e1)
      {
        _try_success ("setMatrix(int[],int[],MatrixInt)... ArrayIndexOutOfBoundsException... ", "");
      }
    }
    catch (final IllegalArgumentException e1)
    {
      fail ("ArrayIndexOutOfBoundsException expected but not thrown");
    }
    try
    {
      B.setMatrix (rowindexset, columnindexset, M);
      try
      {
        _check (M.minus (B.getMatrix (rowindexset, columnindexset)), M);
        _try_success ("setMatrix(int[],int[],MatrixInt)... ", "");
      }
      catch (final RuntimeException e)
      {
        fail ("submatrix not successfully set");
      }
    }
    catch (final ArrayIndexOutOfBoundsException e1)
    {
      fail ("Unexpected ArrayIndexOutOfBoundsException");
    }

    /**
     * Array-like methods: minus minusEquals plus plusEquals arrayLeftDivide
     * arrayLeftDivideEquals arrayRightDivide arrayRightDivideEquals arrayTimes
     * arrayTimesEquals uminus
     **/

    _print ("\nTesting array-like methods...\n");
    MatrixInt S = new MatrixInt (columnwise, nonconformld);
    MatrixInt R = MatrixInt.random (A.getRowDimension (), A.getColumnDimension ());
    A = R;
    try
    {
      S = A.minus (S);
      fail ("nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("minus conformance _check... ", "");
    }
    if (A.minus (R).norm1 () != 0)
    {
      fail ("(difference of identical Matrices is nonzero,\nSubsequent use of minus should be suspect)");
    }
    else
    {
      _try_success ("minus... ", "");
    }
    A = R.getClone ();
    A.minusEquals (R);
    final MatrixInt Z = new MatrixInt (A.getRowDimension (), A.getColumnDimension ());
    try
    {
      A.minusEquals (S);
      fail ("nonconformance not raised");
    }
    catch (final IllegalArgumentException e)
    {
      _try_success ("minusEquals conformance _check... ", "");
    }
    if (A.minus (Z).norm1 () != 0)
    {
      fail ("(difference of identical Matrices is nonzero,\nSubsequent use of minus should be suspect)");
    }
    else
    {
      _try_success ("minusEquals... ", "");
    }

    A = R.getClone ();
    B = MatrixInt.random (A.getRowDimension (), A.getColumnDimension ());
    C = A.minus (B);
    try
    {
      S = A.plus (S);
      fail ("nonconformance not raised");
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
      fail ("(C = A - B, but C + B != A)");
    }
    C = A.minus (B);
    C.plusEquals (B);
    try
    {
      A.plusEquals (S);
      fail ("nonconformance not raised");
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
      fail ("(C = A - B, but C = C + B != A)");
    }
    A = R.uminus ();
    try
    {
      _check (A.plus (R), Z);
      _try_success ("uminus... ", "");
    }
    catch (final RuntimeException e)
    {
      fail ("(-A + A != zeros)");
    }
    A = R.getClone ();
    final MatrixInt O = new MatrixInt (A.getRowDimension (), A.getColumnDimension (), 1);
    C = A.arrayLeftDivide (R);
    try
    {
      S = A.arrayLeftDivide (S);
      fail ("nonconformance not raised");
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
      fail ("(M.\\M != ones)");
    }
    try
    {
      A.arrayLeftDivideEquals (S);
      fail ("nonconformance not raised");
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
      fail ("(M.\\M != ones)");
    }
    A = R.getClone ();
    try
    {
      A.arrayRightDivide (S);
      fail ("nonconformance not raised");
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
      fail ("(M./M != ones)");
    }
    try
    {
      A.arrayRightDivideEquals (S);
      fail ("nonconformance not raised");
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
      fail ("(M./M != ones)");
    }
    A = R.getClone ();
    B = MatrixInt.random (A.getRowDimension (), A.getColumnDimension ());
    try
    {
      S = A.arrayTimes (S);
      fail ("nonconformance not raised");
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
      fail ("(A = R, C = A.*B, but C./B != A)");
    }
    try
    {
      A.arrayTimesEquals (S);
      fail ("nonconformance not raised");
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
      fail ("(A = R, A = A.*B, but A./B != R)");
    }

    /**
     * I/O methods: read print serializable: writeObject readObject
     **/
    _print ("\nTesting I/O methods...\n");
    try
    {
      final DecimalFormat fmt = new DecimalFormat ("0");
      fmt.setDecimalFormatSymbols (DecimalFormatSymbolsFactory.getInstance (Locale.US));

      final PrintWriter aPW = new PrintWriter (new FileOutputStream (FILENAME_JAMA_TEST_MATRIX_OUT));
      A.print (aPW, fmt, 10);
      aPW.close ();
      final BufferedReader aReader = new BufferedReader (new FileReader (FILENAME_JAMA_TEST_MATRIX_OUT));
      R = MatrixInt.read (aReader);
      aReader.close ();
      if (A.minus (R).norm1 () < .001)
      {
        _try_success ("print()/read()...", "");
      }
      else
      {
        fail ("MatrixInt read from file does not match MatrixInt printed to file");
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
        final DecimalFormat fmt = new DecimalFormat ("0");
        final PrintWriter FILE = new PrintWriter (new FileOutputStream (FILENAME_JAMA_TEST_MATRIX_OUT));
        A.print (FILE, fmt, 10);
        FILE.close ();
        final BufferedReader aReader = new BufferedReader (new FileReader (FILENAME_JAMA_TEST_MATRIX_OUT));
        R = MatrixInt.read (aReader);
        aReader.close ();
        if (A.minus (R).norm1 () < .001)
        {
          _try_success ("print()/read()...", "");
        }
        else
        {
          fail ("MatrixInt read from file does not match MatrixInt printed to file");
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

    R = MatrixInt.random (A.getRowDimension (), A.getColumnDimension ());
    final String tmpname = "TMPMATRIX.serial";
    try
    {
      final ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream (tmpname));
      out.writeObject (R);
      out.close ();
      final ObjectInputStream sin = new ObjectInputStream (new FileInputStream (tmpname));
      A = (MatrixInt) sin.readObject ();
      sin.close ();

      try
      {
        _check (A, R);
        _try_success ("writeObject(MatrixInt)/readObject(MatrixInt)...", "");
      }
      catch (final RuntimeException e)
      {
        fail ("MatrixInt not serialized correctly");
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
      fail ("unexpected error in serialization test");
    }
    finally
    {
      FileOperations.deleteFile (new File (tmpname));
    }

    /**
     * LA methods: transpose times cond rank det trace norm1 norm2 normF normInf
     * solve solveTranspose inverse chol eig lu qr svd
     **/

    _print ("\nTesting linear algebra methods...\n");
    A = new MatrixInt (columnwise, 3);
    MatrixInt T = new MatrixInt (tvals);
    T = A.transpose ();
    try
    {
      _check (A.transpose (), T);
      _try_success ("transpose...", "");
    }
    catch (final RuntimeException e)
    {
      fail ("transpose unsuccessful");
    }
    A.transpose ();
    try
    {
      _check (A.norm1 (), columnsummax);
      _try_success ("norm1...", "");
    }
    catch (final RuntimeException e)
    {
      fail ("incorrect norm calculation");
    }
    try
    {
      _check (A.normInf (), rowsummax);
      _try_success ("normInf()...", "");
    }
    catch (final RuntimeException e)
    {
      fail ("incorrect norm calculation");
    }
    try
    {
      _check (A.normF (), Math.sqrt (sumofsquares));
      _try_success ("normF...", "");
    }
    catch (final RuntimeException e)
    {
      fail ("incorrect norm calculation");
    }
    try
    {
      _check (A.trace (), sumofdiagonals);
      _try_success ("trace()...", "");
    }
    catch (final RuntimeException e)
    {
      fail ("incorrect trace calculation");
    }
    final MatrixInt SQ = new MatrixInt (square);
    try
    {
      _check (A.times (A.transpose ()), SQ);
      _try_success ("times(MatrixInt)...", "");
    }
    catch (final RuntimeException e)
    {
      fail ("incorrect MatrixInt-MatrixInt product calculation");
    }
    try
    {
      _check (A.times (0), Z);
      _try_success ("times(double)...", "");
    }
    catch (final RuntimeException e)
    {
      fail ("incorrect MatrixInt-scalar product calculation");
    }

    A = new MatrixInt (columnwise, 4);
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

  private static void _check (final int x, final int y)
  {
    if (x != y)
    {
      throw new RuntimeException ("The difference x-y is !=0: x = " + x + "  y = " + y);
    }
  }

  /** Check norm of difference of "vectors". **/

  private static void _check (final int [] x, final int [] y)
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

  private static void _check (@Nonnull final int [][] x, @Nonnull final int [][] y)
  {
    final MatrixInt A = new MatrixInt (x);
    final MatrixInt B = new MatrixInt (y);
    _check (A, B);
  }

  /** Check norm of difference of Matrices. **/

  private static void _check (@Nonnull final MatrixInt X, @Nonnull final MatrixInt Y)
  {
    if (X.norm1 () == 0. && Y.norm1 () < 10 * EPSILON)
      return;
    if (Y.norm1 () == 0. && X.norm1 () < 10 * EPSILON)
      return;
    if (X.minus (Y).norm1 () > 1000 * EPSILON * Math.max (X.norm1 (), Y.norm1 ()))
      throw new RuntimeException ("The norm of (X-Y) is too large: " + Double.toString (X.minus (Y).norm1 ()));
  }

  /** Shorten spelling of print. **/

  private static void _print (final String s)
  {
    System.out.print (s);
  }

  /** Print appropriate messages for successful outcome try **/

  private static void _try_success (final String s, final String e)
  {
    _print (">    " + s + "success\n");
    if (StringHelper.hasText (e))
      _print (">      Message: " + e + "\n");
  }

  /** Print appropriate messages for unsuccessful outcome try **/

  private static int _try_warning (final int count, final String s, final String e)
  {
    _print (">    " + s + "*** warning ***\n>      Message: " + e + "\n");
    return count + 1;
  }
}
