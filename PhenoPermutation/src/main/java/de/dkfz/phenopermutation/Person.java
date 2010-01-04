package de.dkfz.phenopermutation;

public class Person {
    private final int id;
    private final Haplotype haplo1;
    private final Haplotype haplo2;
    private boolean flag;

    public Person(int id, Haplotype h1, Haplotype h2) {
        this.id = id;
        haplo1 = h1;
        haplo2 = h2;
        setFlag(false);
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * return id 1..n
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    public Haplotype getHaplo1() {
        return haplo1;
    }

    public Haplotype getHaplo2() {
        return haplo2;
    }

    /**
     * pos is needed for array index
     * 
     * @return
     */
    public int getPos() {
        return id - 1;
    }

}
