/**
 * 
 */
package openones.gate.store_test.dao;

import java.util.ArrayList;
import java.util.List;

import openones.gate.store_test.dto.ModuleContentDTO;
import openones.gate.store_test.dto.ModuleManagerDTO;
import openones.gate.store_test.util.OOGHibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author katherine
 * 
 */
public class ModuleManagerDAO extends AbstractDAO {

	static {
		controlledClass = ModuleManagerDTO.class;
	}

	public Boolean insert(ModuleManagerDTO dto) {
		// TODO Auto-generated method stub
		return super.insert(dto);
	}

	public Boolean delete(ModuleManagerDTO dto) {
		// TODO Auto-generated method stub
		return super.delete(dto);
	}

	public Boolean update(ModuleManagerDTO dto) {
		// TODO Auto-generated method stub
		return super.update(dto);
	}

	/*
	 * Get all records on database
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleManagerDTO> getAllList() {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			List<ModuleManagerDTO> objects = (List<ModuleManagerDTO>) session
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
	public List<ModuleManagerDTO> getList(int startIndex, int count) {
		List<ModuleManagerDTO> objects = new ArrayList<ModuleManagerDTO>();
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass)
					.setFirstResult(startIndex).setMaxResults(count);
			objects = (List<ModuleManagerDTO>) c.list();
		} catch (Exception ex) {
		}
		return objects;
	}

	/*
	 * Get ModuleManagerDTO by moduleID and accountID
	 */
	public ModuleManagerDTO getModuleManagerByID(int moduleID, int accountID) {
		ModuleManagerDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass);
			c = c.add(Restrictions.eq("ModuleID", moduleID));
			c = c.add(Restrictions.eq("AccountID", accountID));
			dto = (ModuleManagerDTO) c.uniqueResult();
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
