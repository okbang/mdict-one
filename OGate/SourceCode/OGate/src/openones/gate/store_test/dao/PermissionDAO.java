/**
 * 
 */
package openones.gate.store_test.dao;

import java.util.ArrayList;
import java.util.List;
import openones.gate.store_test.dto.PermissionDTO;
import openones.gate.store_test.util.OOGHibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author katherine
 * 
 */
public class PermissionDAO extends AbstractDAO {

	static {
		controlledClass = PermissionDTO.class;
	}

	public Boolean insert(PermissionDTO dto) {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass);
			c = c.setProjection(Projections.max("PermissionID"));
			PermissionDTO maxDTO = (PermissionDTO) c.uniqueResult();
			dto.setPermissionID(maxDTO.getPermissionID() + 1);
			return super.insert(dto);
		} catch (Exception ex) {
			return false;
		}
	}

	public Boolean delete(PermissionDTO dto) {
		// TODO Auto-generated method stub
		return super.delete(dto);
	}

	public Boolean update(PermissionDTO dto) {
		// TODO Auto-generated method stub
		return super.update(dto);
	}

	/*
	 * Get all records on database
	 */
	@SuppressWarnings("unchecked")
	public List<PermissionDTO> getAllList() {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			List<PermissionDTO> objects = (List<PermissionDTO>) session
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
	public List<PermissionDTO> getList(int startIndex, int count) {
		List<PermissionDTO> objects = new ArrayList<PermissionDTO>();
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass)
					.setFirstResult(startIndex).setMaxResults(count);
			objects = (List<PermissionDTO>) c.list();
		} catch (Exception ex) {
		}
		return objects;
	}

	/*
	 * Get PermissionDTO by permissionID and langCd
	 */
	public PermissionDTO getPermissionByID(int permissionID, String langCd) {
		PermissionDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass);
			c = c.add(Restrictions.eq("PermissionID", permissionID));
			c = c.add(Restrictions.eq("LangCd", langCd));
			dto = (PermissionDTO) c.uniqueResult();
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
