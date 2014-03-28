package dep.dao;

import java.util.List;

import javax.sql.DataSource;

import dep.model.HistoricalTrendEntity;
import dep.model.ProjectBureauEntity;

public interface IDao {

	void setDataSource();

	List<HistoricalTrendEntity> getFilteredHistoricalTrendData();
	
	List<ProjectBureauEntity> getProjectsByBureau(String bureau, String operatingBureau);

}
	