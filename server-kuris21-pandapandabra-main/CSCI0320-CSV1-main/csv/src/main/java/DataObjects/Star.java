package DataObjects;

public class Star {
  private int starID;
  private String Name;
  private double x;
  private double y;
  private double z;


  public Star(int starID, String Name, double x, double y, double z) {
    this.starID = starID;
    this.Name = Name;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  // Getters
  public int getStarID() {
    return starID;
  }

  public String getName() {
    return Name;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  @Override
  public String toString() {
    return "Star{" +
        "starID=" + starID +
        ", properName='" + Name + '\'' +
        ", x=" + x +
        ", y=" + y +
        ", z=" + z +
        '}';
  }
}

