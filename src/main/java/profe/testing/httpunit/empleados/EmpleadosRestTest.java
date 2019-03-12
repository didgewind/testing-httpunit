package profe.testing.httpunit.empleados;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.meterware.httpunit.DeleteMethodWebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpNotFoundException;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import profe.empleados.model.Empleado;

public class EmpleadosRestTest {

	private WebConversation wc;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private interface Constantes {
		String JSON_CONTENT_TYPE = "application/json";
		int CONFLICT_STATUS_CODE = 409;
		int OK_STATUS_CODE = 200;
		int CREATED_STATUS_CODE = 201;
	}
	
	@Before
	public void setUp() {
		wc = new WebConversation();
	}

	@Test
	public void whenGetEmpleadoNoExistenteStatusCodeNotFound() throws Exception {
		exception.expect(HttpNotFoundException.class);
		WebRequest request = new GetMethodWebRequest("http://localhost:5555/empleados/wert");
		wc.getResponse(request);
	}

	@Test
	public void whenDeleteEmpleadoNoExistenteStatusCodeNotFound() throws Exception {
		exception.expect(HttpNotFoundException.class);
		WebRequest request = new DeleteMethodWebRequest("http://localhost:5555/empleados/wert");
		wc.getResponse(request);
	}

	@Test
	public void whenPutEmpleadoNoExistenteStatusCodeNotFound() throws Exception {
		exception.expect(HttpNotFoundException.class);
		InputStream is = 
				HttpUnitUtil.getEmpleadoNoExistenteAsInputStream();
		WebRequest request = new PutMethodWebRequest("http://localhost:5555/empleados/wert", is,
				Constantes.JSON_CONTENT_TYPE);
		wc.getResponse(request);
	}

	@Test
	public void whenPostEmpleadoYaExistenteStatusCodeConflict() throws Exception {
		wc.setExceptionsThrownOnErrorStatus(false);
		InputStream is = 
				HttpUnitUtil.getEmpleadoYaExistenteAsInputStream();
		WebRequest request = new PostMethodWebRequest("http://localhost:5555/empleados/", is,
				Constantes.JSON_CONTENT_TYPE);
		WebResponse response = wc.getResponse(request);
		assertEquals(Constantes.CONFLICT_STATUS_CODE, response.getResponseCode());
	}
	
	@Test
	public void whenPostEmpleadoNuevoStatusCodeCreated() throws Exception {
		wc.setExceptionsThrownOnErrorStatus(false);
		InputStream is = 
				HttpUnitUtil.getEmpleadoAsInputStream(
						new Empleado("234", "Noelia", "Pérez", 22));
		WebRequest request = new PostMethodWebRequest("http://localhost:5555/empleados/", is,
				Constantes.JSON_CONTENT_TYPE);
		WebResponse response = wc.getResponse(request);
		assertEquals(Constantes.CREATED_STATUS_CODE, response.getResponseCode());
		// Limpiamos
		request = new DeleteMethodWebRequest("http://localhost:5555/empleados/234");
		wc.getResponse(request);
	}


}
