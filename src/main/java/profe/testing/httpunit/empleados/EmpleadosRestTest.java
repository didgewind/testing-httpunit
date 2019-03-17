package profe.testing.httpunit.empleados;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meterware.httpunit.DeleteMethodWebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpNotFoundException;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.PutMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import profe.empleados.model.Empleado;
import profe.empleados.model.EmpleadoForTest;

public class EmpleadosRestTest {

	private WebConversation wc;

	private interface Constantes {
		String restUrl = "http://localhost:5555/empleados/";
		String JSON_CONTENT_TYPE = "application/json";
		int CONFLICT_STATUS_CODE = 409;
		int OK_STATUS_CODE = 200;
		int CREATED_STATUS_CODE = 201;
	}
	
	@BeforeEach
	public void setUp() {
		wc = new WebConversation();
	}

	@Test
	public void whenGetEmpleadoNoExistenteThenStatusCodeNotFound() throws Exception {
		WebRequest request = new GetMethodWebRequest(Constantes.restUrl + "wert");
		assertThrows(HttpNotFoundException.class, () -> wc.getResponse(request));
	}

	@Test
	public void whenDeleteEmpleadoNoExistenteThenStatusCodeNotFound() throws Exception {
		WebRequest request = new DeleteMethodWebRequest(Constantes.restUrl + "wert");
		assertThrows(HttpNotFoundException.class, () -> wc.getResponse(request));
	}

	@Test
	public void whenPutEmpleadoNoExistenteThenStatusCodeNotFound() throws Exception {
		InputStream is = 
				HttpUnitUtil.getEmpleadoNoExistenteAsInputStream();
		WebRequest request = new PutMethodWebRequest(Constantes.restUrl + "wert", is,
				Constantes.JSON_CONTENT_TYPE);
		assertThrows(HttpNotFoundException.class, () -> wc.getResponse(request));
	}

	@Test
	public void whenPostEmpleadoYaExistenteThenStatusCodeConflict() throws Exception {
		wc.setExceptionsThrownOnErrorStatus(false);
		InputStream is = 
				HttpUnitUtil.getEmpleadoYaExistenteAsInputStream();
		WebRequest request = new PostMethodWebRequest(Constantes.restUrl + "", is,
				Constantes.JSON_CONTENT_TYPE);
		WebResponse response = wc.getResponse(request);
		assertEquals(Constantes.CONFLICT_STATUS_CODE, response.getResponseCode());
	}
	
	@Test
	public void whenPostEmpleadoThenNuevoStatusCodeCreated() throws Exception {
		InputStream is = 
				HttpUnitUtil.getEmpleadoAsInputStream(getNuevoEmpleado());
		WebRequest request = new PostMethodWebRequest(Constantes.restUrl, is,
				Constantes.JSON_CONTENT_TYPE);
		WebResponse response = wc.getResponse(request);
		assertEquals(Constantes.CREATED_STATUS_CODE, response.getResponseCode());
		// Limpiamos
		request = new DeleteMethodWebRequest(Constantes.restUrl + "234");
		wc.getResponse(request);
	}

	@Test
	public void whenPostEmpleadoThenNuevoEmpleadoCreado() throws Exception {
		EmpleadoForTest empExpected = getNuevoEmpleado();
		InputStream is = 
				HttpUnitUtil.getEmpleadoAsInputStream(empExpected);
		WebRequest request = new PostMethodWebRequest(Constantes.restUrl, is,
				Constantes.JSON_CONTENT_TYPE);
		wc.getResponse(request);
		request = new GetMethodWebRequest(Constantes.restUrl + empExpected.getCif());
		WebResponse response = wc.getResponse(request);
		ObjectMapper mapper = new ObjectMapper();
		EmpleadoForTest empRecibido = mapper.readValue(response.getText(), EmpleadoForTest.class);
		assertEquals(empExpected, empRecibido);
		System.out.println(empRecibido);
		// Limpiamos
		request = new DeleteMethodWebRequest(Constantes.restUrl + "234");
		wc.getResponse(request);
	}

	@Test
	public void whenGetAllEmpleadosThenReceivedLengthIs6() throws Exception {
		WebRequest request = new GetMethodWebRequest(Constantes.restUrl);
		WebResponse response = wc.getResponse(request);
		ObjectMapper mapper = new ObjectMapper();
		Empleado[] empleados = mapper.readValue(response.getText(), Empleado[].class);
		assertEquals(6, empleados.length);
	}

	private static EmpleadoForTest getNuevoEmpleado() {
		return new EmpleadoForTest("234", "Noelia", "Pérez", 22);
	}
	

}
