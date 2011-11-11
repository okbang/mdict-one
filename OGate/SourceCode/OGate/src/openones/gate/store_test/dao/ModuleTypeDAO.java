/**
 * 
 */
package openones.gate.store_test.dao;

import java.util.ArrayList;
import java.util.List;

import openones.gate.store_test.dto.ModuleTypeDTO;
import openones.gate.store_test.util.OOGHibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;


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
	 * Get ModuleTypeDTO by Id
	 */
	public ModuleTypeDTO getModuleTypeByID(int id) {
		ModuleTypeDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			dto = (ModuleTypeDTO) session.get(controlledClass, id);
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
