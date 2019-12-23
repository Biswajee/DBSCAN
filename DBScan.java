//import libraries

import java.util.*;

// Base pixel class
class Pixel {

    int grayValue;
    int coordX;
    int coordY;

    Pixel() {               // default constructor
        this.grayValue = 0;
        this.coordX = 0;
        this.coordY = 0;
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
        return coordX;      // Get X coordinate value of pixel
    }

    int getY() {
        return coordY;      // Get Y coordinate value of pixel
    }
}

// Image Class - defines an image using `Pixel` objects
class Image {

    int width;
    int height;
    int ImgArray[][];

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
    }

    Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.ImgArray = new int[width][height];
    }

    void definePixel(Pixel X) {
        ImgArray[X.getX()][X.getY()] = X.getGrayValue();   // Updates gray value for single pixel on image
    }

    void definePixelArray(Pixel[] X) {
        int i=0, j=0;
        for (int k=0; k < X.length; k++) {
            ImgArray[i][j++] = X[k].getGrayValue();   // Updates gray value for array of pixels on image
            if(j==32) { j=0; i++; }
        }
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

        if(A.coordX-1 < 0 || A.coordY-1 < 0 || A.coordX+1 > Im.width || A.coordY+1 > Im.height) {
            System.out.println("Cannot test boundary pixels for now !");
            return;
        }
        else {
            int seedPixel = Im.getGrayValueFromCoord(A.coordX, A.coordY);
            int northWest = Im.getGrayValueFromCoord(A.coordX-1, A.coordY+1);
            int top = Im.getGrayValueFromCoord(A.coordX, A.coordY+1);
            int northEast = Im.getGrayValueFromCoord(A.coordX+1, A.coordY+1);
            int west = Im.getGrayValueFromCoord(A.coordX-1, A.coordY);
            int east = Im.getGrayValueFromCoord(A.coordX+1, A.coordY);
            int southEast = Im.getGrayValueFromCoord(A.coordX+1, A.coordY-1);
            int southWest = Im.getGrayValueFromCoord(A.coordX-1, A.coordY-1);
            int bottom = Im.getGrayValueFromCoord(A.coordX, A.coordY-1);

            // City block -- need to add euclidean distance and make section flexible

            eightND.add(new pixelDistance(Math.abs(northWest-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.coordX-1, A.coordY+1),A.coordX-1, A.coordY+1)));   // north-west pixel distance
            eightND.add(new pixelDistance(Math.abs(top-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.coordX-1, A.coordY+1),A.coordX-1, A.coordY+1)));         // top pixel distance
            eightND.add(new pixelDistance(Math.abs(northEast-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.coordX+1, A.coordY+1),A.coordX+1, A.coordY+1)));   // north east pixel distance
            eightND.add(new pixelDistance(Math.abs(west-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.coordX-1, A.coordY),A.coordX-1, A.coordY)));        // west pixel distance
            eightND.add(new pixelDistance(Math.abs(east-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.coordX+1, A.coordY),A.coordX+1, A.coordY)));        // east pixel distance
            eightND.add(new pixelDistance(Math.abs(southWest-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.coordX-1, A.coordY-1),A.coordX-1, A.coordY-1)));   // south-west pixel distance
            eightND.add(new pixelDistance(Math.abs(bottom-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.coordX, A.coordY-1),A.coordX, A.coordY-1)));      // bottom pixel distance
            eightND.add(new pixelDistance(Math.abs(southEast-seedPixel), new Pixel(Im.getGrayValueFromCoord(A.coordX+1, A.coordY-1),A.coordX+1, A.coordY-1)));   // south-east pixel distance

            // Select only those pixels lesser than min distance
            Stack<Pixel> minDist = new Stack<>();

            for(pixelDistance p : eightND) {
                if(p.distance <= distance)
                    minDist.push(p.pixel);
            }
            System.out.println(minDist);
            eightNeighbourDistance(Im, minDist.peek(), distance, minPoints);
        }
    }
}

class DBScan {

    public static void main(String args[]) {

        Image image = new Image(32, 32);
        Pixel pixelArr[] = new Pixel[32*32];
        Scanner sc = new Scanner(System.in);
        Distance D = new Distance();
        // Create random valued pixels in array
        for (int i=0, j=0; i < pixelArr.length; i++) {
            pixelArr[i] = new Pixel((int) (Math.random() * 255), i, j++);
            if(j==32) { j=0; }
        }

        // Push pixel array to image
        image.definePixelArray(pixelArr);
        // image.definePixel(new Pixel(2,2,35));

        // Print the image
//        image.printImage();

        System.out.print("Enter the minimum distance: ");
        int distance = sc.nextInt();

        System.out.print("Enter the minimum number of points: ");
        int minPoints = sc.nextInt();

        D.eightNeighbourDistance(image,pixelArr[24],distance,minPoints);


    }
}