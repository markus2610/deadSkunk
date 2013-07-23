/**

 Copyright (c) 2011 Jake Douglas

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

package com.r0adkll.deadskunk.utils;

import java.math.BigInteger;

public class FNV {
  private static final BigInteger INIT32  = new BigInteger("811c9dc5",         16);
  private static final BigInteger INIT64  = new BigInteger("cbf29ce484222325", 16);
  private static final BigInteger PRIME32 = new BigInteger("01000193",         16);
  private static final BigInteger PRIME64 = new BigInteger("100000001b3",      16);
  private static final BigInteger MOD32   = new BigInteger("2").pow(32);
  private static final BigInteger MOD64   = new BigInteger("2").pow(64);

  public BigInteger fnv1_32(byte[] data) {
    BigInteger hash = INIT32;

    for (byte b : data) {
      hash = hash.multiply(PRIME32).mod(MOD32);
      hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
    }

    return hash;
  }

  public BigInteger fnv1_64(byte[] data) {
    BigInteger hash = INIT64;

    for (byte b : data) {
      hash = hash.multiply(PRIME64).mod(MOD64);
      hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
    }

    return hash;
  }

  public BigInteger fnv1a_32(byte[] data) {
    BigInteger hash = INIT32;

    for (byte b : data) {
      hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
      hash = hash.multiply(PRIME32).mod(MOD32);
    }

    return hash;
  }

  public BigInteger fnv1a_64(byte[] data) {
    BigInteger hash = INIT64;

    for (byte b : data) {
      hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
      hash = hash.multiply(PRIME64).mod(MOD64);
    }

    return hash;
  }
}
