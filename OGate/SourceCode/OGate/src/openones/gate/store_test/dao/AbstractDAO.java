/**
 * 
 */
package openones.gate.store_test.dao;

import openones.gate.store_test.util.OOGHibernateUtil;

import org.hibernate.Session;


/**
 * @author katherine
 * 
 */
public abstract class AbstractDAO {

	/*
	 * Controlled class
	 */
	@SuppressWarnings("rawtypes")
	protected static Class controlledClass;

	/*
	 * Insert a object to database
	 */
	public Boolean insert(Object ob) {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			session.persist(ob);
		} catch (Exception ex) {

			return false;
		}
		return true;
	}

	/*
	 * Delete a object from database
	 */
	public Boolean delete(Object ob) {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			session.delete(ob);
		} catch (Exception ex) {

			return false;
		}
		return true;
	}

	/*
	 * Update a object in database
	 */
	public Boolean update(Object ob) {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			session.update(ob);
		} catch (Exception ex) {

			return false;
		}
		return true;
	}
}
