package cern.colt.matrix.linalg;

import java.io.IOException;
import java.util.Arrays;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Mult;

public class EigenDecompositionSparse implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    /* number of columns*/
    private int n;
    /* d and e: diagonal and off diagonal elements of the  hessenbergh matrix*/ 
    private double[] d;
	private double[] e;
    /*V: eigen vectors of H*/
    private double[][] V;
    private DenseDoubleMatrix2D Q;
    private int iter;
    
    public EigenDecompositionSparse(DenseDoubleMatrix2D A) throws IOException{
         Property.DEFAULT.checkSquare(A);
         
         n= A.columns();
         iter = (n>500)? 500 :n;
         V = new double[iter][iter];
         for(int i =0;i<iter;i++)V[i][i]=1;
         d = new double[iter];
         e = new double [iter];
         Q = new DenseDoubleMatrix2D(n,iter);
        
         if(!Property.DEFAULT.isSymmetric(A)) throw new IOException("Error the input matrix must be symmetric");
         
        /* compute H where H is hessenbergh and H = Q'AQ*/
         ArnoldiAlg(A);
         /* implicit QL decomposition using combination of jacobian and given rotations*/
         tql2();
         
         
      }

    private void ArnoldiAlg(DenseDoubleMatrix2D A) {
        /*initialize q1*/
        DenseDoubleMatrix1D q =  new DenseDoubleMatrix1D(n);
        q.assign(1.0);
        double norm_q =(float)Math.sqrt(q.zDotProduct(q));
        q =(DenseDoubleMatrix1D) q.assign(Mult.div(norm_q));
        System.out.println("check1");
        System.out.println(q);
        
        DenseDoubleMatrix1D r =  (DenseDoubleMatrix1D) A.zMult(q, null);
        double aj =(float) q.zDotProduct(r);
        System.out.println("check2");
        System.out.println(r);
        System.out.println(aj);
        
       
        r = (DenseDoubleMatrix1D) r.assign(q.copy().assign(Mult.mult(aj)), cern.jet.math.PlusMult.minusMult(1.0));
        double bj = (float)Math.sqrt(r.zDotProduct(r));
        System.out.println("check3");
        System.out.println(bj);
        System.out.println(r);
        
       
        d[0]=aj;
        e[0]=bj;
        Q.viewColumn(0).assign(q);
       
        for (int i=1;i<iter;i++){
            DenseDoubleMatrix1D v  =q;
            q= (DenseDoubleMatrix1D) r.assign(Mult.div(bj));
            Q.viewColumn(i).assign(q);
            System.out.println();
            System.out.println("check4");
            System.out.println("q" +(i-1) +" "+v);
            System.out.println("q" + (i) + " " +q);
           
            System.out.println("check5");
            r= (DenseDoubleMatrix1D) A.zMult(q, null);
            System.out.println("r = A*q"+i+" "+r);
            System.out.println(bj);
            System.out.println(v.copy().assign(Mult.mult(bj)));
            System.out.println("check 6");
            r = (DenseDoubleMatrix1D) r.assign(v.copy().assign(Mult.mult(bj)), cern.jet.math.PlusMult.minusMult(1.0));
            System.out.println("Aq"+i+ "-bjqi-1 "+ r);
            System.out.println();
            aj = q.zDotProduct(r);
            System.out.println(aj);
            d[i]=aj;
           
            r = (DenseDoubleMatrix1D) r.assign(q.copy().assign(Mult.mult(aj)), cern.jet.math.PlusMult.minusMult(1.0));
            System.out.println("Aq"+i+ "-ajqi "+ r);
            /* re-orthogonalisation*/
            if(i>1){
                DenseDoubleMatrix2D ortho = (DenseDoubleMatrix2D)Q.zMultTranspose();
                DenseDoubleMatrix1D r_p = (DenseDoubleMatrix1D) ortho.zMult(r, null);
                r =(DenseDoubleMatrix1D) r.copy().assign(r_p,cern.jet.math.PlusMult.minusMult(1.0));
            }
            
            bj = (float)Math.sqrt(r.zDotProduct(r));
            System.out.println(bj);
            if (i!=(iter-1))e[i]=bj;
            if ((double)(int)(bj*100000000)==0) break;
          }
          e[iter-1]=0;
      }
    private void tql2 () {

        //  This is derived from the Algol procedures tql2, by
        //  Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
        //  Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
        //  Fortran subroutine in EISPACK.
       
          n=iter;
          System.out.println(Arrays.toString(e));
          //e[iter-1] = (float) 0.0;
       
          double f = 0;
          double tst1 = 0;
          double eps = Math.pow(2.0,-52.0);
          for (int l = 0; l < n; l++) {
             // Find small sub-diagonal element
             tst1 = Math.max(tst1,Math.abs(d[l]) + Math.abs(e[l]));
             int m = l;
             while (m < n) {
                if (Math.abs(e[m]) <= eps*tst1) {
                   break;
                }
                m++;
             }
             // If m == l, d[l] is an eigenvalue,
             // otherwise, iterate.
             
             if (m > l) {
                int iter = 0;
                do {
                   iter = iter + 1;  // (Could check iteration count here.)
                   
                   // Compute implicit shift through Jacobian rotation
                   double g = d[l];
                   /* p is theta/cot2phi*/
                   double p =  ((d[l+1] - g) / (2.0 * e[l]));
                   
                   /*t = 1/sign(theta)+ sqrt(theta^2+1))*/
                   double r = Algebra.hypot(p,1.0);
                   if (p < 0) {
                      r = -r;
                   }
                   
                   /* d[l]= e[l]*t*/
                   d[l] =  (e[l] / (p + r));
                   d[l+1] =  (e[l] * (p + r));
                   double dl1 = d[l+1];
                   double h = g - d[l];
                   for (int i = l+2; i < n; i++) {
                      d[i] -= h;//d[i]= d[i]-ks
                   }
                   /* f at l=0 is equal to k_s and is the sum of Sum ks for all l*/
                   
                   f = f + h;
       
                   // Implicit QL transformation.
                  
                   p = d[m];
                   
                   double c = 1.0;
                   double c2 = c;
                   double c3 = c;
                   double el1 = e[l+1];
                   double s = 0.0;
                   double s2 = 0.0;
                   /* 1 Jacobian rotation followed by Given rotations*/
                   for (int i = m-1; i >= l; i--) {
                      c3 = c2;
                      c2 = c;
                      s2 = s;
                      /* for i=m-1, c2,c3,c=1 and s=0*/
                      g =  (c * e[i]);
                      h =  (c * p);
                      /* r  = sqrt( e[m-1]^2 + d[m]^2) for i =m-1*/
                      r =  Algebra.hypot(p,e[i]);
                      /* for i=m-1 e[i+1]= 0  */
                      e[i+1] =  (s * r);
                      s = e[i] / r;
                      c = p / r;

                      p =  (c * d[i] - s * g);
                      d[i+1] =  (h + s * (c * g + s * d[i]));
                        // Accumulate transformation.
                        for (int k = 0; k < n; k++) {
                        	 h = V[k][i+1];
        					 V[k][i+1] = s * V[k][i] + c * h;
        					 V[k][i] = c * V[k][i] - s * h;
                      }
                   }
                   
                   p =  (-s * s2 * c3 * el1 * e[l] / dl1);
                   e[l] =  (s * p);
                   d[l] =  (c * p);
                   // Check for convergence.
       
                } while (Math.abs(e[l]) > eps*tst1);
             }
             d[l] =  (d[l] + f);
             e[l] =  0.0;
          }
        // Sort eigenvalues and corresponding vectors.
          for (int i=0;i<d.length;i++){
              int max= i;
              for(int k =i;k<d.length;k++){
                  if (d[k]>d[max]) max =k;
              }
              double tmp ;
              tmp=d[i];
              d[i]= d[max];
              d[max] =tmp;
              for (int j=0;j<n;j++){
                  tmp =  V[j][i];
                  V[j][i]=V[j][max];
                  V[j][max]=tmp;
              }
          }
          
          
         
       } 
    
    public DenseDoubleMatrix2D getV(){
           System.out.println(Q);
           
           return (DenseDoubleMatrix2D) Q.zMult(new DenseDoubleMatrix2D(V), null);
    }
    public double[] getSingularValues(){
        return d;
        
    }
    
    public static void main(String[] args){
    	double[] p = new double[]{0,0,0,0,0, 0,1,0,0,0, 0,0,2,0,0, 0,0,0,3,0, 0,0,0,0,9999};
    	DoubleFactory2D fact = DoubleFactory2D.dense;
    	DenseDoubleMatrix2D matrix  = (DenseDoubleMatrix2D) fact.make(p,5);
    	EigenvalueDecomposition eig = new EigenvalueDecomposition(matrix);
    	System.out.println(Arrays.toString(eig.getSingularvalues()));
    	System.out.println(eig.getV());
    	
    	EigenDecompositionSparse eigen;
		try {
			eigen = new EigenDecompositionSparse(matrix);
			System.out.println(Arrays.toString(eigen.getSingularValues()));
			System.out.println(eigen.getV());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 double  d =0.000000001;
		 System.out.println((double)(int)(d*100000000));

    	
    }
    
    
    

    
    
}
