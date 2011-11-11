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


/**
 * @author katherine
 *
 */
public class PermissionDAO extends AbstractDAO {


	static {
		controlledClass = PermissionDTO.class;
	}

	public Boolean insert(PermissionDTO dto) {
		// TODO Auto-generated method stub
		return super.insert(dto);
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
	 * Get PermissionDTO by Id
	 */
	public PermissionDTO getPermissionByID(int id, String langCd) {
		PermissionDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			//session.createCriteria(controlledClass).
			dto = (PermissionDTO) session.get(controlledClass, id);
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
