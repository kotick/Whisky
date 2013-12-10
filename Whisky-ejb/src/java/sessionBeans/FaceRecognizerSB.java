/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.Stateless;
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

    private FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
    //FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
    // FaceRecognizer faceRecognizer = createLBPHFaceRecognizer()
    public FaceRecognizerSB(){
        
        
    }
    
     @Override
    public void train(){
         //this.faceRecognizer = createEigenFaceRecognizer();
    
         System.out.println("Comienza el entrenamiento del predictor");        
        String trainingDir = "C:\\fotos\\test\\";
       // IplImage testImage = cvLoadImage("/Users/kotick/NetBeansProjects/Whisky/prueba.jpg");
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

       // IplImage greyTestImage = IplImage.create(testImage.width(), testImage.height(), IPL_DEPTH_8U, 1);
        

        faceRecognizer.train(images, labels);
    
    }
    
     @Override
    public void test(String ruta_foto, int [] id, double [] distancia){
    
       System.out.println("Comienza el reconocimiento de imagen");
       
       IplImage testImage = cvLoadImage(ruta_foto);

       IplImage greyTestImage = IplImage.create(testImage.width(), testImage.height(), IPL_DEPTH_8U, 1);
    
        cvCvtColor(testImage, greyTestImage, CV_BGR2GRAY);
        
        faceRecognizer.predict(greyTestImage, id, distancia);
        
    }

     @Override
    public boolean predict(String ruta_foto, long id){
            
      
        int [] id_test = {-1};
        double []distancia = {0.0};
        double distancia_minima= 20000;
        
        this.train();
        this.test(ruta_foto, id_test, distancia);
        System.out.println("Label");
        System.out.println(id_test[0]);
        System.out.println("Confidence");
        System.out.println(distancia[0]);
        if (id_test[0] <distancia_minima && id == id_test[0]){
            return true;
        }
        else{
        
            return false;
        }
        //double[] acercamiento = {};
       
       
    
    }
    
  
    
    
 
}
