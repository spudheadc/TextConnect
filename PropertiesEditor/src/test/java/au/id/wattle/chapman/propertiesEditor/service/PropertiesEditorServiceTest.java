/**
 * 
 */
package au.id.wattle.chapman.propertiesEditor.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

import au.id.wattle.chapman.propertiesEditor.model.TreeNode;

/**
 * @author chris
 * 
 */
public class PropertiesEditorServiceTest {

	@Mock
	private WritableResource resource;

	@Mock
	private WritableResource fileResource;

	@InjectMocks
	private PropertiesEditorServiceImpl service;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetTree() throws Exception {
		String str = "a.c=value 2\na.d.e=value 3\na.d.f=value 4\na.b = value 1\nb.a = value ad\n";
		when(fileResource.getFilename()).thenReturn("filename");
		when(fileResource.getInputStream()).thenReturn(
				new ByteArrayInputStream(str.getBytes()));
		List<Resource> list = new ArrayList<Resource>();
		list.add(fileResource);
		service.setFiles(list);
		
		List<TreeNode> nodes = service.getTree();

		assertNotNull(nodes);
		TreeNode node = nodes.get(0);

		assertNotNull(node);
		assertEquals("a", node.getLabel());

		TreeNode bNode = node.getChildren().get(0);
		assertNotNull(bNode);
		assertEquals("b", bNode.getLabel());
		assertEquals("value 1", bNode.getValue().get("filename"));
		
		TreeNode cNode = node.getChildren().get(1);
		assertNotNull(cNode);
		assertEquals("c", cNode.getLabel());
		assertEquals("value 2", cNode.getValue().get("filename"));

		TreeNode dNode = node.getChildren().get(2);
		assertEquals("d", dNode.getLabel());
		assertNotNull(dNode);


		TreeNode eNode = dNode.getChildren().get(0);
		assertNotNull(eNode);
		assertEquals("e", eNode.getLabel());
		assertEquals("value 3", eNode.getValue().get("filename"));
		
		TreeNode fNode = dNode.getChildren().get(1);
		assertNotNull(fNode);
		assertEquals("f", fNode.getLabel());
		assertEquals("value 4", fNode.getValue().get("filename"));

		node = nodes.get(1);
		assertEquals("b", node.getLabel());
		assertEquals("a", node.getChildren().get(0).getLabel());
	}

	@Test
	public void testSetTree() throws Exception {
		String str = "a.b = b value\na.c=c value\n";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		when(fileResource.getFilename()).thenReturn("filename");
		when(fileResource.getOutputStream()).thenReturn(baos);
		List<Resource> fileList = new ArrayList<Resource>();
		fileList.add(fileResource);
		service.setFiles(fileList);
		
		List<TreeNode> list = new ArrayList<TreeNode>();
		list.add(getTestTree());
		service.setTree(list);

		String outputString = baos.toString(StandardCharsets.UTF_8.toString());

		// We compare Properties sets so we ignore comments and order
		assertEquals(createFromString(str), createFromString(outputString));
	}

	private static Properties createFromString(String str) throws IOException {

		Properties properties = new Properties();
		properties.load(new ByteArrayInputStream(str.getBytes()));

		return properties;
	}

	private Map getMap(String value) {

		Map<String, String> map = new TreeMap<String, String>();
		map.put("filename", value);
		return map;
	}

	private TreeNode getTestTree() {
		TreeNode aNode = new TreeNode();
		aNode.setLabel("a");
		TreeNode bNode = new TreeNode();
		bNode.setValue(getMap("b value"));
		bNode.setLabel("b");
		aNode.setChildren(new ArrayList<TreeNode>());
		aNode.getChildren().add( bNode);
		
		TreeNode cNode = new TreeNode();
		cNode.setValue(getMap("c value"));
		cNode.setLabel("c");
		aNode.getChildren().add(cNode);
		return aNode;
	}

}
