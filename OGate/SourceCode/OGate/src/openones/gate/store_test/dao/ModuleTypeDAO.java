/**
 * 
 */
package openones.gate.store_test.dao;

import java.util.ArrayList;
import java.util.List;

import openones.gate.store_test.dto.ModuleContentDTO;
import openones.gate.store_test.dto.ModuleTypeDTO;
import openones.gate.store_test.util.OOGHibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author katherine
 * 
 */
public class ModuleTypeDAO extends AbstractDAO {

	static {
		controlledClass = ModuleTypeDTO.class;
	}

	public Boolean insert(ModuleTypeDTO dto) {
		// TODO Auto-generated method stub
		return super.insert(dto);
	}

	public Boolean delete(ModuleTypeDTO dto) {
		// TODO Auto-generated method stub
		return super.delete(dto);
	}

	public Boolean update(ModuleTypeDTO dto) {
		// TODO Auto-generated method stub
		return super.update(dto);
	}

	/*
	 * Get all records on database
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleTypeDTO> getAllList() {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			List<ModuleTypeDTO> objects = (List<ModuleTypeDTO>) session
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
	public List<ModuleTypeDTO> getList(int startIndex, int count) {
		List<ModuleTypeDTO> objects = new ArrayList<ModuleTypeDTO>();
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass)
					.setFirstResult(startIndex).setMaxResults(count);
			objects = (List<ModuleTypeDTO>) c.list();
		} catch (Exception ex) {
		}
		return objects;
	}

	/*
	 * Get ModuleTypeDTO by moduleTypeID and langCd
	 */
	public ModuleTypeDTO getModuleTypeByID(int moduleTypeID, String langCd) {
		ModuleTypeDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass);
			c = c.add(Restrictions.eq("ModuleTypeID", moduleTypeID));
			c = c.add(Restrictions.eq("LangCd", langCd));
			dto = (ModuleTypeDTO) c.uniqueResult();
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
