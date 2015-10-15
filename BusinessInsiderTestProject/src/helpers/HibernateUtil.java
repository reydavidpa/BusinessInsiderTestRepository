package helpers;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();
	private static final SessionFactory HibMSSQLSessionFactory = buildHibernateMSSQLSessionFactory();
	
	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			// Eclipse says that the buildSessionFactory is deprecated, however there is no mention of this in the javadoc
			// http://docs.jboss.org/hibernate/stable/annotations/api/org/hibernate/cfg/Configuration.html#buildSessionFactory()
			return new Configuration().configure("\\hibernate\\hibernate.cfg.xml").buildSessionFactory();
		}
		catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.out.println("Initial SessionFactory buildSessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	
	private static SessionFactory buildHibernateMSSQLSessionFactory() {
		try {
			return new Configuration().configure("\\hibernate\\hibernateSQLServer.cfg.xml").buildSessionFactory();
		}
		catch (Throwable ex) {
			System.out.println("Initial CompanySessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	public static SessionFactory getHibernateMSSQLSessionFactory() {
		return HibMSSQLSessionFactory;
	}
	public static void closeSessionFactory(SessionFactory fs){
		fs.close();
	}
	public static void closeAllSessionFactories(){
		sessionFactory.close();
		HibMSSQLSessionFactory.close();
	}


}
