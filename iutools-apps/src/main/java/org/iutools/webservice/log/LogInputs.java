package org.iutools.webservice.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.iutools.webservice.ServiceException;
import org.iutools.webservice.ServiceInputs;
import org.json.JSONObject;

import java.util.Map;


/**
 * Specifies the details of a UI task to be logged.
 */
public class LogInputs extends ServiceInputs {

	public static enum Action {SPELL, GIST_TEXT, GIST_WORD, SEARCH_WEB;
	}

	public Action action = null;
	public Map<String,Object> taskData = null;

	public LogInputs() throws ServiceException {
		init_LogInputs((Action)null, (JSONObject)null);
	}

	public LogInputs(Action _action, JSONObject _taskData) throws ServiceException {
		init_LogInputs(_action, _taskData);
	}

	public LogInputs(Action _action, Map<String,Object> _taskData) throws ServiceException {
		init_LogInputs(_action, _taskData);
	}

	private void init_LogInputs(Action _action, JSONObject _taskInputs)
		throws ServiceException {

		if (_taskInputs != null) {
			String json = _taskInputs.toString();
			try {
				Map<String,Object> taskDataMap =
					new ObjectMapper().readValue(json, Map.class);
				init_LogInputs(action, taskDataMap);
			} catch (JsonProcessingException e) {
				throw new ServiceException(e);
			}
		}

		return;
	}

	private void init_LogInputs(Action _action, Map<String,Object> _taskData)
		throws ServiceException {

		this.action = _action;
		this.taskData = _taskData;

		return;
	}
}
