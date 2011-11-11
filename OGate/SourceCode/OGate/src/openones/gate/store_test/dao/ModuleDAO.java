/**
 * 
 */
package openones.gate.store_test.dao;

import java.util.ArrayList;
import java.util.List;

import openones.gate.store_test.dto.ModuleContentDTO;
import openones.gate.store_test.dto.ModuleDTO;
import openones.gate.store_test.util.OOGHibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author katherine
 * 
 */
public class ModuleDAO extends AbstractDAO {

	static {
		controlledClass = ModuleDTO.class;
	}

	public Boolean insert(ModuleDTO dto) {
		// TODO Auto-generated method stub
		return super.insert(dto);
	}

	public Boolean delete(ModuleDTO dto) {
		// TODO Auto-generated method stub
		return super.delete(dto);
	}

	public Boolean update(ModuleDTO dto) {
		// TODO Auto-generated method stub
		return super.update(dto);
	}

	/*
	 * Get all records on database
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleDTO> getAllList() {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			List<ModuleDTO> objects = (List<ModuleDTO>) session.createCriteria(
					controlledClass).list();
			return objects;
		} catch (Exception ex) {

			return null;
		}
	}

	/*
	 * Get records with startIndex and count of records on database
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleDTO> getList(int startIndex, int count) {
		List<ModuleDTO> objects = new ArrayList<ModuleDTO>();
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass)
					.setFirstResult(startIndex).setMaxResults(count);
			objects = (List<ModuleDTO>) c.list();
		} catch (Exception ex) {
		}
		return objects;
	}

	/*
	 * Get ModuleDTO by moduleID and langCd
	 */
	public ModuleDTO getModuleByID(int moduleID, String langCd) {
		ModuleDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass);
			c = c.add(Restrictions.eq("ModuleID", moduleID));
			c = c.add(Restrictions.eq("LangCd", langCd));
			dto = (ModuleDTO) c.uniqueResult();
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
