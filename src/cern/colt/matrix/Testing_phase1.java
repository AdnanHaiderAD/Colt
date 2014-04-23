package cern.colt.matrix;



import cern.colt.Arrays;
import cern.colt.map.OpenIntDoubleHashMap;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.doublealgo.Statistic;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenDecompositionSparse;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import cern.colt.matrix.linalg.SingularValueDecomposition;
import cern.jet.math.Mult;

import java.io.*;
import java.util.Random;
public class Testing_phase1 {
	static Testing_phase1 test= new Testing_phase1();
	protected Testing_phase1(){};
	
	
	public static void create_matricestest(){
		/*testing and creating matrices*/
		DoubleMatrix2D matrix;
		matrix = new DenseDoubleMatrix2D(3,4);
		//matrix = new SparseDoubleMatrix2D(3,4);
		System.out.println(matrix);
		/* checks the reference associated with the class object calling hello*/
		test.hello();
		
	}
	public static void interactiveMatrixbuilderTest(){
		/*interactive response*/
		System.out.println("Enter ur name: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String response = null;
		try {
		 response =br.readLine();
		}catch(IOException ioe){
			System.out.println("IO error");
		}
		boolean wantDense= response.equals("dense");
		
		
		DoubleFactory2D factory;
		if (wantDense) factory= DoubleFactory2D.dense;
		else factory=DoubleFactory2D.sparse;
		
		DoubleMatrix2D matrix = factory.make(5, 7);
		System.out.println(matrix);
		System.out.println(matrix.rows());
		System.out.println(matrix.columns());
	}
	
	public static void matrixTransposeTest(DoubleMatrix2D matrix){
		/*performs matrix transpose*/
		matrix =matrix.viewDice();
		System.out.println(matrix);
		
	}
	public static DoubleMatrix2D createRandomMatrixTest(String type,int rows,int columns){
		DoubleFactory2D factory;
		DoubleMatrix2D matrix;
		factory= type.equals("sparse") ?  DoubleFactory2D.sparse : DoubleFactory2D.dense;
		/*creates a random matrix*/
		matrix= factory.make(rows, columns);
		if ((matrix.getClass().getSimpleName().toString()).equals("SparseDoubleMatrix2D")){
			System.out.println("the matrix uis "+ matrix.getClass().getSimpleName());
			for ( int row=0; row<rows;row++){
				for (int column=0;column<columns;column++){
					Random generator= new Random();
					double value = generator.nextFloat();
					matrix.setQuick(row, column,value=value<0.5 ? 0:value*10);
				}
			}/*optimise memory usage*/
		matrix.trimToSize();
		}
		else{
			for ( int row=0; row<rows;row++){
				for (int column=0;column<columns;column++){
					Random generator= new Random();
					double value = generator.nextFloat();
					matrix.setQuick(row, column,value);
				}
		    }
		}
		return matrix;
		
	}
	public static void extractVectorTest(DoubleMatrix2D matrix, int row){
		/*extracts vectors from matrices*/
		DoubleMatrix1D vect= matrix.viewRow(2);
		System.out.println(vect);
		
	}
	public static void createMatrixFromVectorsTest(DoubleMatrix1D[] list){
		DoubleFactory2D factory= DoubleFactory2D.dense;
	    DoubleMatrix2D matrix= factory.augment(list);
	    System.out.println(matrix);
	}
	
	
	public static void MapTest(){
		/*testing the map*/
		int[]keys ={0,3,1,2,10000};
		int[] values ={100,1000,10000,10000,1};
		OpenIntDoubleHashMap map = new OpenIntDoubleHashMap(2);
		
		for(int i=0;i<keys.length;i++){
			map.put(keys[i], values[i]);
			System.out.println("\nmap="+map);
			System.out.println("size="+map.size());
		
			// remove one association
			map.removeKey(3);
			System.out.println("map="+map);
			System.out.println(map.containsKey(3));
			System.out.println("get(3)="+map.get(3));
			

		}
		System.out.println("\nmap="+map);
	}
	public static void sVD (DoubleMatrix2D matrix){
		SingularValueDecomposition decomposition= new SingularValueDecomposition(matrix);
		System.out.println("the U matrix is" + decomposition.getU() );
		System.out.println("The singular values are "+ decomposition.getS());
		System.out.println("the V matrix is "+decomposition.getV());
	}
	public static void eigenTest(DoubleMatrix2D matrix){
		/* takes a rectangular matrix as input and returns the V matrix from X= U*D*V'&*/
		long  start = System.currentTimeMillis();
		DoubleMatrix2D matrixT= matrix.viewDice().copy();
	    DoubleMatrix2D output = matrixT.zMult(matrix, null);	
	    System.out.println("the time taken is  "+(System.currentTimeMillis()-start));
		   
	      start= System.currentTimeMillis();
	     EigenvalueDecomposition eigen =  new EigenvalueDecomposition(output);
	     System.out.println("the time taken is eg "+(System.currentTimeMillis()-start));
	   
	}
	public static void main(String[] args) throws InterruptedException, IOException{
		
		DoubleFactory2D factory = DoubleFactory2D.dense;
		double[] p= new double []{ 1,0,1,1,0,2,0,1,0};
		  DoubleMatrix2D m =factory.make(p,3);
		  System.out.println(m);
		  System.out.println(cern.colt.matrix.doublealgo.Statistic.angulardistance(m));
		/*float [] q = new float[]{1,0,0,3,1,2,3};
		DoubleMatrix2D matrix =factory.make(p,3);
		DoubleMatrix1D q1 = new DenseDoubleMatrix1D(q);
		System.out.println(q1.assign(Mult.div(4)));
		System.out.println(matrix.zMult(q1, null));
		System.out.println("matrix");
		System.out.println(matrix);
		System.out.println(matrix.viewPart(0, 0, 3, 2));
		
		
	    
	    DoubleMatrix2D matT =matrix.zMultTranspose();
	    DenseDoubleMatrix1D w = new DenseDoubleMatrix1D(new float []{ 1 ,2, 3});
	    System.out.println("check");
	    System.out.println(matT.zMult(w, null));
	    System.out.println(matrix);
	   
	    System.out.println("matrix transpose");
	    System.out.println(matT);
	   // System.out.println(Statistic.distance(matT.viewDice(), Statistic.EUCLID));
	    
	    EigenvalueDecomposition eigen =  new EigenvalueDecomposition(matT);
	    System.out.println("the v matrix:");
	    System.out.println(eigen.getV());
	    DoubleMatrix2D v = eigen.getV().assignOptimised(cern.jet.math.Functions.abs);
	    float[] s= eigen.getRealEigenvalues().toArray();
	    System.out.println(Arrays.toString(s));
	    
		
		*/
		
		/* creating a big sparse matrix while minimising space*/
		System.out.println(cern.colt.matrix.doublealgo.Statistic.roundToSignificantFigures(0.23456789000023, 5));
		DenseDoubleMatrix2D matrix=(DenseDoubleMatrix2D) createRandomMatrixTest("dense", 10000, 500);
		DenseDoubleMatrix2D matT =(DenseDoubleMatrix2D) matrix.zMultTranspose();
		System.out.println("transpose computation completed");
        
		long time2 =System.currentTimeMillis();
        EigenDecompositionSparse eig = new EigenDecompositionSparse(matT);
        DenseDoubleMatrix2D V=eig.getV();
        System.out.println("time taken by new method is "+(System.currentTimeMillis()-time2));
        double[] s= java.util.Arrays.copyOfRange(eig.getSingularValues(),0,100);
        System.out.println("finished"+(Arrays.toString(s)));
		
        //saving the ritz vectors
        FileOutputStream out = new FileOutputStream(new File("/home/adnan/workspace_test/AdnanColt/outtestfiles/V2.ser"));
        ObjectOutputStream outStream = new ObjectOutputStream(out);
        outStream.writeObject(V);
        outStream.close();
        out.close();
        
        long time =  System.currentTimeMillis();
		EigenvalueDecomposition eigen=new EigenvalueDecomposition(matT);
		time= (System.currentTimeMillis()-time);
		System.out.println("time taken by old method is "+time);
		s= java.util.Arrays.copyOfRange(eigen.getRealEigenvalues().toArray(), 9900, 10000);
	    System.out.println(Arrays.toString(s));
       
        
		
		
	}
	public void hello(){
		if (this==test){
			System.out.println("hello");
		}
	}
}
