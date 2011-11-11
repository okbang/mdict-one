/**
 * 
 */
package openones.gate.store_test.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author katherine
 *
 */
public class OOGHibernateUtil {

	private static final SessionFactory sessionFactory;
	
	static {
		try{
			
			sessionFactory = new Configuration().configure().buildSessionFactory();
			
		}catch(Throwable ex){
			throw new  ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
}
