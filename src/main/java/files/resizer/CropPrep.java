package files.resizer;

public class CropPrep {
    public int x;
    public int y;
    public int side;

    /**
     * top corner coordinates + side (it's a square)
     */
    public CropPrep(int x, int y, int side){
        this.x = x;
        this.y = y;
        this.side = side;
    }
}
