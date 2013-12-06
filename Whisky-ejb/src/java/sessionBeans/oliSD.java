/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import javax.ejb.Stateless;

/**
 *
 * @author Kay
 */
@Stateless
public class oliSD implements oliSDLocal {

   public oliSD(){}
   
   @Override
   public int oli(){
    return 1234;
    }

}
