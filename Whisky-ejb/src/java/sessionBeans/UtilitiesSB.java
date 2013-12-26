/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import DTOs.AttendanceDTO;
import JpaControllers.ParticipantJpaController;
import entity.Participant;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kay
 */
@Stateless
public class UtilitiesSB implements UtilitiesSBLocal {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @PersistenceContext(unitName = "Whisky-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public UtilitiesSB() {
    }

    @Override
    public LinkedList<AttendanceDTO> selectPasswordByEmail(String email) {

        Collection<Participant> resultQuery;
        LinkedList<AttendanceDTO> result = new LinkedList<>();
        AttendanceDTO userDTOTemp;
        Query q = em.createNamedQuery("Participant.getParticipantByEmail", Participant.class);
        q.setParameter("username", email);

        resultQuery = (Collection<Participant>) q.getResultList();
        for (Participant iter : resultQuery) {
            userDTOTemp = new AttendanceDTO();
            userDTOTemp.setPassword(iter.getPassword());
            result.add(userDTOTemp);
        }
        return result;
    }

    @Override
    public Long selectFirstIdByEmail(String email) {

        Collection<Participant> resultQuery;
        LinkedList<AttendanceDTO> result = new LinkedList<>();
        AttendanceDTO userDTOTemp;
        Query q = em.createNamedQuery("Participant.getParticipantByEmail", Participant.class);
        q.setParameter("username", email);

        resultQuery = (Collection<Participant>) q.getResultList();
        for (Participant iter : resultQuery) {
            userDTOTemp = new AttendanceDTO();
            userDTOTemp.setId(iter.getId());
            result.add(userDTOTemp);
        }
        return result.getFirst().getId();

    }

    @Override
    public String stringToMD5(String clear) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(clear.getBytes());

        int size = b.length;
        StringBuffer h = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            int u = b[i] & 255;
            if (u < 16) {
                h.append("0").append(Integer.toHexString(u));
            } else {
                h.append(Integer.toHexString(u));
            }
        }
        return h.toString();
    }

    @Override
    public boolean validateRut(String rut) {
        boolean validate = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validate = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validate;

    }

    @Override
    public boolean validateEmail(String email) {
        try {
            // Compiles the given regular expression into a pattern.
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            // Match the given input against this pattern
            Matcher matcher = pattern.matcher(email);
            Participant resultQuery;
            Query q = em.createNamedQuery("Participant.getParticipantByEmail", Participant.class);
            q.setParameter("username", email);
            if (matcher.matches()) {
                try {
                    resultQuery = (Participant) q.getSingleResult();
                    return false;
                } catch (Exception ex) {
                    return true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
