//import libraries
import java.util.*;

// Base pixel class
class Pixel {

    private int grayValue;
    private int coordX;
    private int coordY;
    private boolean visited;
    Pixel() {               // default constructor
        this.grayValue = 0;
        this.coordX = 0;
        this.coordY = 0;
        this.visited = false;
    }

    Pixel(int grayValue, int coordX, int coordY) {
        this.grayValue = grayValue;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    int getGrayValue() {
        return grayValue;   // Get Gray value of pixel
    }
    int getX() {
        return coordX;              // Get X coordinate value of pixel
    }
    int getY() {
        return coordY;              // Get Y coordinate value of pixel
    }

    boolean isVisited() { return visited; }         // Checks if pixel is DFS visited
    void setVisited() { this.visited = true; }      // Sets pixel as DFS visited
}

// Image Class - defines an image using `Pixel` objects
class Image {

    int width;
    int height;
    int ImgArray[][];
    boolean visited[];
    Image() {
        this.width = 255;
        this.height = 255;
        this.ImgArray = new int[width][height];
        // Creates default image, gray values=0 for all pixels
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; i++) {
                ImgArray[i++][j++] = 0;
            }
        }
        visited = new boolean[width*height];
    }

    Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.ImgArray = new int[width][height];
        visited = new boolean[width*height];
    }

    void definePixel(Pixel X) {
        ImgArray[X.getX()][X.getY()] = X.getGrayValue();   // Updates gray value for single pixel on image
        visited[X.getX()*X.getY()] = X.isVisited();        // Updates visited status of the pixel
    }

    void definePixelArray(Pixel[] X) {
        int i=0, j=0;
        for (int k=0; k < X.length; k++) {
            ImgArray[i][j++] = X[k].getGrayValue();   // Updates gray value for array of pixels on image
            if(j==(int)Math.sqrt(X.length)) { j=0; i++; }
        }
        // TODO: Section for registering visited pixels
    }

    // prints the image array for a given image object
    void printImage() {
        for (int i=0; i < width; i++) {
            for (int j=0; j < height; j++) {
                System.out.print(ImgArray[i][j] + " ");
            }
            System.out.println();
        }
    }
    int getGrayValueFromCoord(int x,int y) {
        return ImgArray[x][y];
    }

    void printLabels() {
        int i=0;
        for(int j=0; j<width; j++) {
            for(int k=0; k<height; k++) {
                if (visited[i++] == false)
                    System.out.print("0\t");
                else System.out.print("1\t");
            }
            System.out.println();
        }
    }
}

// Structure to store distance and pixel...for Distance class
class pixelDistance {
        /*
         * (x-1,y+1)  (x,y+1)  (x+1,y+1)
         * (x-1,y)    (x,y)    (x+1,y)
         * (x-1,y-1)  (x,y-1)  (x+1,y-1)
         */
    int distance;
    Pixel pixel;
    pixelDistance(int distance, Pixel pixel) {
        this.distance = distance;
        this.pixel = pixel;
    }
}

// Distance measure for image clustering algorithms
class Distance {
    Stack<Pixel> minDist = new Stack<>();
    // Pixel A: Seed pixel, Pixel B: 8 Neighbour pixel
    double euclidian(Pixel A, Pixel B) {
        return Math.sqrt(Math.pow(Math.abs(A.getGrayValue() - B.getGrayValue()), 2));
    }

    // double chessboard(Pixel A, Pixel B) {
    //     return Math.max(A.getGrayValue(), B.getGrayValue());
    // }

    double cityblock(Pixel A, Pixel B) {
        return Math.abs(A.getGrayValue() - B.getGrayValue());
    }

    void eightNeighbourDistance(Image Im, Pixel A, int distance, int minPoints) {
        /*
         * (x-1,y+1)  (x,y+1)  (x+1,y+1)
         * (x-1,y)    (x,y)    (x+1,y)
         * (x-1,y-1)  (x,y-1)  (x+1,y-1)
         */
        Vector<pixelDistance> eightND = new Vector<>();

        if(A.getX()-1 < 0 || A.getY()-1 < 0 || A.getX()+1 > Im.width || A.getY()+1 > Im.height || A.isVisited()==true) {
            for(Pixel p : minDist) {
                System.out.println("("+p.getX()+","+p.getY()+","+p.getGrayValue()+","+p.isVisited()+")");
            }

            // Set pixel visited status to image class
            for(Pixel p : minDist) {
                Im.definePixel(p);
            }

            if(!minDist.isEmpty()) minDist.pop();


            System.out.println("Success ! ");
        }
        else {
            int seedPixel = Im.getGrayValueFromCoord(A.getX(), A.getY());
            int northWest = Im.getGrayValueFromCoord(A.getX()-1, A.getY()+1);
            int top = Im.getGrayValueFromCoord(A.getX(), A.getY()+1);
            int northEast = Im.getGrayValueFromCoord(A.getX()+1, A.getY()+1);
            int west = Im.getGrayValueFromCoord(A.getX()-1, A.getY());
            int east = Im.getGrayValueFromCoord(A.getX()+1, A.getY());
            int southEast = Im.getGrayValueFromCoord(A.getX()+1, A.getY()-1);
            int southWest = Im.getGrayValueFromCoord(A.getX()-1, A.getY()-1);
            int bottom = Im.getGrayValueFromCoord(A.getX(), A.getY()-1);

            // City block -- need to add euclidean distance and make section flexible

            eightND.add(new pixelDistance(Math.abs(northWest-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.getX()-1, A.getY()+1),A.getX()-1, A.getY()+1)));   // north-west pixel distance
            eightND.add(new pixelDistance(Math.abs(top-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.getX()-1, A.getY()+1),A.getX()-1, A.getY()+1)));         // top pixel distance
            eightND.add(new pixelDistance(Math.abs(northEast-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.getX()+1, A.getY()+1),A.getX()+1, A.getY()+1)));   // north east pixel distance
            eightND.add(new pixelDistance(Math.abs(west-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.getX()-1, A.getY()),A.getX()-1, A.getY())));        // west pixel distance
            eightND.add(new pixelDistance(Math.abs(east-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.getX()+1, A.getY()),A.getX()+1, A.getY())));        // east pixel distance
            eightND.add(new pixelDistance(Math.abs(southWest-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.getX()-1, A.getY()-1),A.getX()-1, A.getY()-1)));   // south-west pixel distance
            eightND.add(new pixelDistance(Math.abs(bottom-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.getX(), A.getY()-1),A.getX(), A.getY()-1)));      // bottom pixel distance
            eightND.add(new pixelDistance(Math.abs(southEast-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.getX()+1, A.getY()-1),A.getX()+1, A.getY()-1)));   // south-east pixel distance

            // Select only those pixels lesser than min distance

            for(pixelDistance p : eightND) {
                if(p.distance <= distance) {
                    p.pixel.setVisited(); // setting pixel as visited, so will not be traversed again.
                    minDist.push(p.pixel);
                }
            }

            eightNeighbourDistance(Im, minDist.pop(), distance, minPoints);
        }
    }
}

class DBScan {
    public static void main(String args[]) {

        int imWidth = 3;
        int imHeight = 3;
        Image image = new Image(imWidth, imHeight);
        Pixel pixelArr[] = new Pixel[imWidth*imHeight];
        Scanner sc = new Scanner(System.in);
        Distance D = new Distance();
        // Create random valued pixels in array
//        for (int i=0, j=0, k=0; i < pixelArr.length; i++) {
//            pixelArr[i] = new Pixel((int) (Math.random() * 255), k, j++);
//            if(j==imHeight) { j=0; k++;}
//        }


        // Custom pixel array 3x3
        /*  __               __
         * |                   |
         * | 50     60      70 |
         * | 40     20      1  |
         * | 40     80      20 |
         * |                   |
         *  ¯¯               ¯¯
         */
        pixelArr[0] = new Pixel(50,0,0);
        pixelArr[1] = new Pixel(60,0,1);
        pixelArr[2] = new Pixel(70,0,2);
        pixelArr[3] = new Pixel(40,1,0);
        pixelArr[4] = new Pixel(20,1,1);
        pixelArr[5] = new Pixel(01,1,2);
        pixelArr[6] = new Pixel(40,2,0);
        pixelArr[7] = new Pixel(30,2,1);
        pixelArr[8] = new Pixel(20,2,2);


        // Push pixel array to image
        image.definePixelArray(pixelArr);
        // image.definePixel(new Pixel(2,2,35));

        // Print the image
//        image.printImage();

        System.out.print("Enter the minimum distance: ");
        int distance = sc.nextInt();

        System.out.print("Enter the minimum number of points: ");
        int minPoints = sc.nextInt();

        System.out.println("\n\n#############################INPUT IMAGE ARRAY#############################");
        for(int i=0; i<pixelArr.length; i++) {
            if(i%imWidth==0) System.out.println();
            System.out.print("(" + pixelArr[i].getX() + ", " + pixelArr[i].getY() + ", " + pixelArr[i].getGrayValue() + ")    ");
        }
        System.out.println("\n#########################END OF INPUT IMAGE ARRAY##########################\n\n\n");

        D.eightNeighbourDistance(image,pixelArr[4],distance,minPoints);

        // Print Image labels
        System.out.println("\n\n##############################BEGIN PRINTING LABELS###########################");
        image.printLabels();
        System.out.println("\n#####################################END LABELS###############################");
    }
}