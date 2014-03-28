package dep.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class ProjectBureauRowMapper implements RowMapper  {
	
	public Object mapRow(ResultSet rs, int line) throws SQLException {
		ProjectBureauEntity projectBureau = new ProjectBureauEntity();
		projectBureau.setProjectId(rs.getString("Project_ID"));
		projectBureau.setOperatingBureau(rs.getString("Bureau"));
		return projectBureau;
	}

}
