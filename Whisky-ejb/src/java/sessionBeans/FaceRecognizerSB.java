/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.Stateless;
import com.googlecode.javacv.cpp.opencv_core;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_contrib.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Kay
 */
@Stateless
public class FaceRecognizerSB implements FaceRecognizerSBLocal {

    public FaceRecognizerSB(){
    
    }
    
    @Override
    public int prueba(){
        System.out.println("empece la prueba de face");
        
        String trainingDir = "C:\\fotos\\test\\";
        IplImage testImage = cvLoadImage("C:\\fotos\\prueba.jpg");

        File root = new File(trainingDir);

        FilenameFilter jpgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        };

        File[] imageFiles = root.listFiles(jpgFilter);

        MatVector images = new MatVector(imageFiles.length);

        int[] labels = new int[imageFiles.length];

        int counter = 0;
        int label;

        IplImage img;
        IplImage grayImg;

        for (File image : imageFiles) {
            img = cvLoadImage(image.getAbsolutePath());

            label = Integer.parseInt(image.getName().split("\\-")[0]);

            grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);

            cvCvtColor(img, grayImg, CV_BGR2GRAY);

            images.put(counter, grayImg);

            labels[counter] = label;

            counter++;
        }

        IplImage greyTestImage = IplImage.create(testImage.width(), testImage.height(), IPL_DEPTH_8U, 1);

        //FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
         FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        // FaceRecognizer faceRecognizer = createLBPHFaceRecognizer()

        faceRecognizer.train(images, labels);

        cvCvtColor(testImage, greyTestImage, CV_BGR2GRAY);

        int prediccion = faceRecognizer.predict(greyTestImage);
        double[] acercamiento = {};
       
        return prediccion;
    
    }
    
    @Override
    public int oli(){
    return 1234;
    }
}
