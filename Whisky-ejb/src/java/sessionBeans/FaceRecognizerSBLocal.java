/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.Local;

/**
 *
 * @author Kay
 */
@Local
public interface FaceRecognizerSBLocal {




    public void test(String ruta_foto, int[] id, double[] distancia);

    public void train();

    public boolean predict(String ruta_foto, long id);

    


}
