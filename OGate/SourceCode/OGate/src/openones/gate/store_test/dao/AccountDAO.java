/**
 * 
 */
package openones.gate.store_test.dao;

import java.util.ArrayList;
import java.util.List;

import openones.gate.store_test.dto.AccountDTO;
import openones.gate.store_test.util.OOGHibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author katherine
 * 
 */
public class AccountDAO extends AbstractDAO {

	static {
		controlledClass = AccountDTO.class;
	}

	public Boolean insert(AccountDTO dto) {
		// TODO Auto-generated method stub
		return super.insert(dto);
	}

	public Boolean delete(AccountDTO dto) {
		// TODO Auto-generated method stub
		return super.delete(dto);
	}

	public Boolean update(AccountDTO dto) {
		// TODO Auto-generated method stub
		return super.update(dto);
	}

	/*
	 * Get all records on database
	 */
	@SuppressWarnings("unchecked")
	public List<AccountDTO> getAllList() {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			List<AccountDTO> objects = (List<AccountDTO>) session
					.createCriteria(controlledClass).list();
			return objects;
		} catch (Exception ex) {

			return null;
		}
	}

	/*
	 * Get records with startIndex and count of records on database
	 */
	@SuppressWarnings("unchecked")
	public List<AccountDTO> getList(int startIndex, int count) {
		List<AccountDTO> objects = new ArrayList<AccountDTO>();
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass)
					.setFirstResult(startIndex).setMaxResults(count);
			objects = (List<AccountDTO>) c.list();
		} catch (Exception ex) {
		}
		return objects;
	}

	/*
	 * Get AccountDTO by AccountID
	 */
	public AccountDTO getAccountByAccountID(int accountID) {
		AccountDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass);
			c = c.add(Restrictions.eq("AccountID", accountID));
			dto = (AccountDTO) c.uniqueResult();
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
