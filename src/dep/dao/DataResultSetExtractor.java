package dep.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;

import dep.model.HistoricalTrendEntity;

public class DataResultSetExtractor implements ResultSetExtractor  {
	
	public Object extractData(ResultSet rs) throws SQLException {
		HistoricalTrendEntity entity = new HistoricalTrendEntity();
		Long P6_Orig_Proj_id = rs.getLong(4);
//		System.out.println("P6_Orig_Proj_id: " + P6_Orig_Proj_id);
		if(P6_Orig_Proj_id==0)//current project
		{
			entity.setDataPeriod(rs.getLong(1));
			entity.setP6_Proj_id(rs.getLong(2));
			entity.setProjectId(rs.getString(3));
//			entity.setP6_Orig_Proj_id(rs.getString(4));
			entity.setCurrentNTP(rs.getString(5));
			entity.setCurrentSC(rs.getString(6));
			entity.setCurrentStage(rs.getString(7));
			entity.setId(entity.getDataPeriod().toString()+entity.getP6_Proj_id().toString());
		}
		else//baseline
		{
			//find the right object
			entity.setDataPeriod(rs.getLong(1));
			entity.setP6_Proj_id(rs.getLong(2));
			entity.setProjectId(rs.getString(3));
			entity.setP6_Orig_Proj_id(P6_Orig_Proj_id);
			entity.setBaselineNTP(rs.getString(5));
			entity.setBaselineSC(rs.getString(6));
			entity.setId(entity.getDataPeriod().toString()+entity.getP6_Proj_id().toString());
		}
//		System.out.println(entity.getProjectId()+": "+entity.getId());
		return entity;
	}

}
