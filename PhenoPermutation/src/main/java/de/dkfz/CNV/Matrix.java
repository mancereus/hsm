package de.dkfz.CNV;

import java.util.Random;

public class Matrix {

    Random rand = new java.util.Random();

    int ncol;// number of columns
    int nrow;// number of rows
    int[] mdim = new int[2];// mdim[0] = nrow
    int dimension;// mdim[1] = ncol
    double[][] mat;// matrix of double values
    double msum;// sum of all matrix values
    double mmean;// mean of all values of the matrix

    /*
     * useless constructor
     */
    public Matrix(int n) {

        mat = new double[n][n];

    }

    /*
     * constructor based on a double matrix with following properties (cf
     * checkMatrix)
     * 
     * 
     * iv) for the examples given here, the matrices have to be normalized, i.e.
     * sum = mean = 0
     */
    public Matrix(double[][] dmat) {

        mat = dmat;
        nrow = dmat.length;
        ncol = dmat[0].length;
        checkMatrix();
        dimension = ncol;
        mdim[0] = nrow;
        mdim[1] = ncol;
        // msum = calculateMsum();
        // mmean = calculateMmean();
        // normalizeMatrix();

    }

    /**
     * check whether the matrix is ok i) symmetric, mat[i][j] == mat[j][i] ii)
     * ncol = nrow iii) diagonal elements are zero mat[i][j] == 0
     */
    private void checkMatrix() {

        if (this.ncol != this.nrow) {

            System.err.println("Matrix has wrong dimension!");
            System.exit(-1);

        }

        for (int i = 0; i < this.nrow; i++) {

            if (this.mat[i][i] != 0) {

                System.err.println("Matrix has diagonal not equal zero!");
                System.exit(-1);

            }

            for (int j = i + 1; j < this.ncol; j++) {

                if (this.mat[i][j] != this.mat[j][i]) {

                    System.err.println("Matrix not symmetric!");
                    System.exit(-1);

                }

            }

        }

    }

    /**
     * print matrix entries to standard output
     */
    public void printMatrix() {

        for (int i = 0; i < this.dimension; i++) {

            for (int j = 0; j < this.dimension; j++) {

                System.out.print(this.mat[i][j] + "\t");

            }

            System.out.println();

        }

    }

    /**
     * Addition of the matrix M with a double value
     * 
     * @param scalar
     * @return M + scalar := (mij+scalar)
     */
    private double[][] matrixAdditionScalar(double scalar) {

        int n = this.dimension;
        double tmp;

        double[][] mat0 = new double[n][n];

        for (int i = 0; i < n - 1; i++) {

            mat0[i][i] = 0.0;

            for (int j = i + 1; j < n; j++) {

                tmp = this.getValue(i, j) + scalar;
                mat0[i][j] = tmp;
                mat0[j][i] = tmp;

            }

        }

        mat0[n - 1][n - 1] = 0.0;

        return mat0;

    }

    /**
     * Addition of the matrix M with a double value
     * 
     * @param scalar
     * @return M * scalar := (mij*scalar)
     */
    private double[][] matrixMultiplicationScalar(double scalar) {

        int n = this.dimension;
        double tmp;

        double[][] mat0 = new double[n][n];

        for (int i = 0; i < n - 1; i++) {

            for (int j = i + 1; j < n; j++) {

                tmp = this.getValue(i, j) * scalar;
                mat0[i][j] = tmp;
                mat0[j][i] = tmp;

            }

        }

        return mat0;

    }

    /**
	 * 
	 */
    private void normalizeMatrix() {

        this.mat = this.matrixAdditionScalar(-this.mmean);
        this.msum = 0.0;
        this.mmean = 0.0;

    }

    /**
     * Elementwise multiplication of two matrices X an Y
     * 
     * @param y
     * @return M = (xij*yij)
     */
    public Matrix matrixMultiplication(Matrix y) {

        double[][] m = new double[this.nrow][this.ncol];
        double val;

        int n = this.dimension;

        for (int i = 0; i < (n - 1); i++) {

            m[i][i] = 0;

            for (int j = i + 1; j < n; j++) {

                val = this.mat[i][j] * y.mat[i][j];
                m[i][j] = val;
                m[j][i] = val;

            }

        }

        m[n - 1][n - 1] = 0.0;

        Matrix mm = new Matrix(m);

        return mm;

    }

    /**
     * @param n
     * @return vector with the numbers 0 to (n-1) in random order
     */
    private int[] getPermutedvector(int n) {

        int[] sample = new int[n];

        for (int i = 0; i < sample.length; i++)
            sample[i] = i;

        // for (int i = 0; i < sample.length; i++) {
        // System.out.print(sample[i]);
        // }
        // System.out.println();

        for (int i = 0; i < n; i++) {
            int j = (int) (rand.nextDouble() * n);
            int temp = sample[i];
            sample[i] = sample[j];
            sample[j] = temp;
        }

        // for (int i = 0; i < sample.length; i++) {
        // System.out.print(sample[i]);
        // }
        // System.out.println();

        return sample;

    }

    /**
     * Permutes a matrix based on random permutations of both rows and columns.
     * If the original matrix is symmetric with zero diagonal elements, the
     * resulting matrix is also symmetric with zero diagonal
     * 
     * @return
     */
    public Matrix permuteMatrix() {

        int n = this.dimension;

        double value;

        double[][] zmat = new double[n][n];

        int[] newOrder = getPermutedvector(n);

        for (int i = 0; i < (n - 1); i++) {

            zmat[i][i] = 0.0;

            for (int j = i + 1; j < n; j++) {

                value = this.getValue(newOrder[i], newOrder[j]);

                zmat[i][j] = value;
                zmat[j][i] = value;

            }

        }

        zmat[n - 1][n - 1] = 0.0;

        Matrix z = new Matrix(zmat);

        return z;
    }

    /**
     * Estimates an empirical p-value for the test statistic M = sum(i) sum(y)
     * Xij*Yij by permuting one of two matrices (here y). The one-sided p-value
     * is defined as the number of permutations with p = {# permutation P: M_P >
     * M}
     * 
     * @param y
     * @param seed
     * @param NP
     * @return p-value
     */
    public double calculateTestPermutation(Matrix y, int seed, int NP) {

        rand.setSeed(seed);

        double p = 0;

        /*
         * calculate test statistic M for X and Y M = sum(i) sum(j!=i) Xij*Yij
         */
        double M = this.calculateM(y);

        double Mp;

        for (int i = 0; i < NP; i++) {

            /*
             * permute matrix y, so that both rows and columns are permuted in
             * the same way. The resulting matrix z is symmetric with zero
             * diagonals.
             */
            Matrix z = y.permuteMatrix();
            /*
             * calculate permuted test statistic Mp for x and z Mp = sum(i)
             * sum(j!=i) XijZij
             */
            Mp = this.calculateM(z);
            /*
             * test whether the permuted test statistic is lower or greater than
             * the original one
             */
            p = Mp > M ? p + 1.0 : p;

        }

        p /= NP;

        return p;

    }

    /**
     * Test statistic based on the assumption of asymptotic normality. Formulas
     * follows the description by Mantel, 1967, Cancer Research. The exact
     * permutational expectation E and the variance V are calculated based on
     * matrix operations. The test statistic t is defined as t = (M-E)/sqrt(V)
     */

    public double calculateTestAlternative(Matrix y) {

        int n = this.dimension;

        double V = 0.0;
        double E = 0.0;
        double M = 0.0;
        double t = 0.0;

        /*
         * calculate the values A, B, G, H, K ...
         */
        double[] valsx = this.calculateABDGHK();
        double[] valsy = y.calculateABDGHK();

        double ax = valsx[0];
        double bx = valsx[1];
        double gx = valsx[3];
        double hx = valsx[4];
        double kx = valsx[5];

        double ay = valsy[0];
        double by = valsy[1];
        double gy = valsy[3];
        double hy = valsy[4];
        double ky = valsy[5];

        /*
         * M = test statistic: sum(i) sum (j!=i) XijYij
         */
        M = this.calculateM(y);
        /*
         * E = expectation: 1/n(n-1) * sum(i)sum(j!=i) Xij * sum(i)sum(j!=i) Yij
         */
        E = ax * ay / (n * (n - 1));
        /*
         * V = Variance
         */
        V = (2 * bx * by + 4 * hx * hy / (n - 2) + kx * ky / ((n - 2) * (n - 3)) - gx * gy / (n * (n - 1)))
                / (n * (n - 1));
        /*
         * t = M - E / sqrt(V), test statistic under the assumption of
         * asymptotic normality
         */
        t = (M - E) / Math.sqrt(V);

        // System.out.println("ax = " + ax + " ay = " + ay);
        // System.out.println("bx = " + bx + " by = " + by);
        // System.out.println("gx = " + gx + " gy = " + gy);
        // System.out.println("hx = " + hx + " hy = " + hy);
        // System.out.println("kx = " + kx + " ky = " + ky);
        System.out.println("M = " + M + " E = " + E + " V = " + V);

        if ((new Double(t)).isNaN()) {

            System.err.println("Test is NaN!");
            System.out.println("M = " + M + " E = " + E + " V = " + V);
            System.exit(-1);

        }

        return t;

    }

    /**
     * calculate the values A, B, D, G, H, and K following the formulas
     * described by Mantel, 1967, Cancer Research a = sum of all entries of the
     * matrix b = sum of the squares of all entries d = sum of the squares of
     * the row/column sums h = d - b g = a * a k = g + 2*b - 4*d
     */
    private double[] calculateABDGHK() {

        int n = this.dimension;

        double a = 0.0;
        double b = 0.0;
        double d = 0.0;
        double h = 0.0;
        double k = 0.0;
        double g = 0.0;
        double value = 0.0;
        double tmp = 0.0;

        for (int i = 0; i < (n - 1); i++) {

            tmp = 0.0;

            for (int j = i + 1; j < n; j++) {

                value = this.mat[i][j];
                tmp += value;
                a += value;
                b += value * value;

            }

            for (int j = 0; j < i; j++) {

                tmp += this.mat[i][j];

            }

            d += tmp * tmp;

        }

        a = 2 * a;
        b = 2 * b;

        g = a * a;
        h = d - b;
        k = g + 2 * b - 4 * d;

        double[] v = new double[6];
        v[0] = a;
        v[1] = b;
        v[2] = d;
        v[3] = g;
        v[4] = h;
        v[5] = k;

        return v;
    }

    /**
     * Calculates the test statistic M = sum(i) sum(j!=i) XijYij
     * 
     * @param y
     * @return M
     */
    private double calculateM(Matrix y) {

        Matrix tmp = this.matrixMultiplication(y);

        int n = tmp.dimension;

        double M = 0.0;

        for (int i = 0; i < n - 1; i++) {

            for (int j = i + 1; j < n; j++) {

                M += tmp.mat[i][j];

            }

        }

        M *= 2;

        return M;
    }

    /**
     * Calculates the sum of all entries of a symmetric matrix with zero
     * diagonals
     * 
     * @return
     */
    private double calculateMsum() {

        double mn = 0;

        int n = this.dimension;

        for (int i = 0; i < (n - 1); i++) {

            for (int j = i + 1; j < n; j++) {

                mn += this.mat[i][j];

            }

        }

        mn = 2 * mn;

        return mn;
    }

    public double[][] getMat() {
        return mat;
    }

    public void setMat(double[][] mat) {
        this.mat = mat;
    }

    public double getMsum() {
        return msum;
    }

    public void setMsum(double msum) {
        this.msum = msum;
    }

    public double getMmean() {
        return mmean;
    }

    public void setMmean(double mmean) {
        this.mmean = mmean;
    }

    private double calculateMmean() {

        int d = this.dimension;
        int dn = d * (d - 1);
        double ms = this.msum / dn;

        return ms;

    }

    public int getNcol() {
        return ncol;
    }

    public int getNrow() {
        return nrow;
    }

    public int[] getMdim() {
        return mdim;
    }

    public void setMdim(int[] mdim) {
        this.mdim = mdim;
    }

    private void setValue(int i, int j, double val) {

        this.mat[i][j] = val;
        this.mat[j][i] = val;

    }

    private double getValue(int i, int j) {

        return this.mat[i][j];

    }

}