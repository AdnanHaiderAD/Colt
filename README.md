Colt
====

Open Source Libraries for High Performance Scientific and Technical Computing

http://acs.lbl.gov/software/colt/

Version 1.2.0

License:

 Packages cern.colt* , cern.jet*, cern.clhep

    Copyright (c) 1999 CERN - European Organization for Nuclear Research.

    Permission to use, copy, modify, distribute and sell this software and its documentation
    for any purpose is hereby granted without fee, provided that the above copyright notice appear
    in all copies and that both that copyright notice and this permission notice appear in supporting
    documentation. CERN makes no representations about the suitability of this software for any purpose.
    It is provided "as is" without expressed or implied warranty.

Packages hep.aida.*

    Written by Pavel Binko, Dino Ferrero Merlino, Wolfgang Hoschek, Tony Johnson, Andreas Pfeiffer, and
    others. Check the FreeHEP home page for more info. Permission to use and/or redistribute this work
    is granted under the terms of the LGPL License, with the exception that any usage related to military
    applications is expressly forbidden. The software and documentation made available under the terms of
    this license are provided with no warranty.
    
    The following classes have been added to the Colt library by Adnan Haider
    
    EigenDecompositionSparse: This implements the Arnoldi Algorithm followed by the implicit Q algorithm to extract the 
    eigenvectors corresponding with the largest eigenvalues within few iterations while approximating the eigenvectors 
    associated with small eigenvalues. It compute time is much longer than the standard Eigendecomposition class and is 
    ideal for BIG matrices. 


    The following classes in cern.colt package has been modified by Adnan Haider to improve the performance of large 
    matrix computations.

    In the DoubleMatrix2D class:
    -zmultTranspose() has been added to compute the product of a double matrix with its transpose efficiently

    The Eigenvaluedecomposition class has been modified to speed up reading values from  the given matrix

    The OpenintdoubleHashMap in the map package in colt has been modified : 
    -an overloaded version of the forEachPair method has been added  to apply a given  procedure to sections of the array 
    specified by  the start and end index.

    -the class is equipped to include the lookup tables that memorizes the hash index of each entry in  matrix

    In the DoubleMatrix class :
    - modifications has been made to  the assign(function f) to improve  speed of computation.

    In the SparseDoubleMatrix2D class:
    the zmult function has been modified to make use of available processors in a machine when computing the product of 
    two matrices.


