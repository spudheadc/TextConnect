package au.id.wattle.chapman.propertiesEditor.controllers;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

import au.id.wattle.chapman.propertiesEditor.model.TreeNode;
import au.id.wattle.chapman.propertiesEditor.service.PropertyEditorService;

@RunWith(SpringRunner.class)
@WebMvcTest(PropertiesEditorController.class)
public class PropertiesEditorControllerTest {


    @Autowired
	private MockMvc mockMvc;

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@MockBean
	private PropertyEditorService service;

	@Test
	public void testGetValues() throws Exception {

		List<TreeNode> list = new ArrayList<TreeNode>();
		list.add(getTestTree());
		when(service.getTree()).thenReturn(list);
		mockMvc.perform(get("/editableProperties")).andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8.toString()))
				.andExpect(jsonPath("$", isA(List.class))).andExpect(jsonPath("$[0].label", equalToIgnoringCase("a")))
				.andExpect(jsonPath("$[0].children", isA(List.class)))
				.andExpect(jsonPath("$[0].children[0].value.blah", equalToIgnoringCase("b value")));

	}

	@Test
	public void testPutValues() throws Exception {

		List<TreeNode> list = new ArrayList<TreeNode>();
		list.add(getTestTree());

		when(service.setTree(any(List.class))).thenReturn(list);
		String json = (new Gson()).toJson(list);
		mockMvc.perform(put("/editableProperties").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8.toString()))
				.andExpect(jsonPath("$", isA(List.class))).andExpect(jsonPath("$[0].label", equalToIgnoringCase("a")))
				.andExpect(jsonPath("$[0].children", isA(List.class)))
				.andExpect(jsonPath("$[0].children[0].value.blah", equalToIgnoringCase("b value")));

	}

	private Map getMap(String value) {

		Map<String, String> map = new TreeMap<String, String>();
		map.put("blah", value);
		return map;
	}

	private TreeNode getTestTree() {
		TreeNode aNode = new TreeNode();
		aNode.setLabel("a");
		TreeNode bNode = new TreeNode();
		bNode.setValue(getMap("b value"));
		bNode.setLabel("b");
		aNode.setChildren(new ArrayList<TreeNode>());
		aNode.getChildren().add(bNode);

		TreeNode cNode = new TreeNode();
		cNode.setValue(getMap("c value"));
		cNode.setLabel("c");
		aNode.getChildren().add(cNode);
		return aNode;
	}
}
