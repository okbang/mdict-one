/**
 * 
 */
package openones.gate.store_test.dao;

import java.util.ArrayList;
import java.util.List;

import openones.gate.store_test.dto.AccountDTO;
import openones.gate.store_test.dto.ModuleContentDTO;
import openones.gate.store_test.util.OOGHibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author katherine
 * 
 */
public class ModuleContentDAO extends AbstractDAO {

	static {
		controlledClass = ModuleContentDTO.class;
	}

	public Boolean insert(ModuleContentDTO dto) {
		// TODO Auto-generated method stub
		return super.insert(dto);
	}

	public Boolean delete(ModuleContentDTO dto) {
		// TODO Auto-generated method stub
		return super.delete(dto);
	}

	public Boolean update(ModuleContentDTO dto) {
		// TODO Auto-generated method stub
		return super.update(dto);
	}

	/*
	 * Get all records on database
	 */
	@SuppressWarnings("unchecked")
	public List<ModuleContentDTO> getAllList() {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			List<ModuleContentDTO> objects = (List<ModuleContentDTO>) session
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
	public List<ModuleContentDTO> getList(int startIndex, int count) {
		List<ModuleContentDTO> objects = new ArrayList<ModuleContentDTO>();
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass)
					.setFirstResult(startIndex).setMaxResults(count);
			objects = (List<ModuleContentDTO>) c.list();
		} catch (Exception ex) {
		}
		return objects;
	}

	/*
	 * Get ModuleContentDTO by moduleID and langCd
	 */
	public ModuleContentDTO getModuleContentByModuleIDAndLangCd(int moduleID,
			String langCd) {
		ModuleContentDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			Criteria c = session.createCriteria(controlledClass);
			c = c.add(Restrictions.eq("ModuleID", moduleID));
			c = c.add(Restrictions.eq("LangCd", langCd));
			dto = (ModuleContentDTO) c.uniqueResult();
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
