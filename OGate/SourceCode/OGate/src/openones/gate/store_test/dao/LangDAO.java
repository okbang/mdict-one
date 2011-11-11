/**
 * 
 */
package openones.gate.store_test.dao;

import java.util.List;

import openones.gate.store_test.dto.LangDTO;
import openones.gate.store_test.util.OOGHibernateUtil;

import org.hibernate.Session;


/**
 * @author katherine
 * 
 */
public class LangDAO extends AbstractDAO {

	static {
		controlledClass = LangDTO.class;
	}

	public Boolean insert(LangDTO dto) {
		// TODO Auto-generated method stub
		return super.insert(dto);
	}

	public Boolean delete(LangDTO dto) {
		// TODO Auto-generated method stub
		return super.delete(dto);
	}

	public Boolean update(LangDTO dto) {
		// TODO Auto-generated method stub
		return super.update(dto);
	}

	/*
	 * Get all records on database
	 */
	@SuppressWarnings("unchecked")
	public List<LangDTO> getAllList() {
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			List<LangDTO> objects = (List<LangDTO>) session.createCriteria(
					controlledClass).list();
			return objects;
		} catch (Exception ex) {

			return null;
		}
	}

	/*
	 * Get LangDTO by Id
	 */
	public LangDTO getLangByID(int id) {
		LangDTO dto = null;
		try {
			Session session = OOGHibernateUtil.getSessionFactory()
					.getCurrentSession();
			dto = (LangDTO) session.get(controlledClass, id);
		} catch (Exception ex) {
			dto = null;
		}
		return dto;
	}
}
