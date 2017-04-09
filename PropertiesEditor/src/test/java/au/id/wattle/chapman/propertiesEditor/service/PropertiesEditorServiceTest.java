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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

import au.id.wattle.chapman.propertiesEditor.model.TreeNode;
import au.id.wattle.chapman.propertiesEditor.model.ValueNode;

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

	private String getValueForFilename(TreeNode node, String filename) {
		if (node.getValues() != null) {
			for (Iterator<ValueNode> iterator = node.getValues().iterator(); iterator
					.hasNext();) {
				ValueNode valueNode = iterator.next();
				if (valueNode != null && valueNode.getFilename() != null
						&& valueNode.getFilename().equals(filename)) {
					return valueNode.getValue();
				}
			}
		}
		return null;
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
		assertEquals("value 1", getValueForFilename(bNode,"filename"));
		
		TreeNode cNode = node.getChildren().get(1);
		assertNotNull(cNode);
		assertEquals("c", cNode.getLabel());
		assertEquals("value 2", getValueForFilename(cNode,"filename"));

		TreeNode dNode = node.getChildren().get(2);
		assertEquals("d", dNode.getLabel());
		assertNotNull(dNode);


		TreeNode eNode = dNode.getChildren().get(0);
		assertNotNull(eNode);
		assertEquals("e", eNode.getLabel());
		assertEquals("value 3", getValueForFilename(eNode,"filename"));
		
		TreeNode fNode = dNode.getChildren().get(1);
		assertNotNull(fNode);
		assertEquals("f", fNode.getLabel());
		assertEquals("value 4", getValueForFilename(fNode,"filename"));

		node = nodes.get(1);
		assertEquals("b", node.getLabel());
		assertEquals("a", node.getChildren().get(0).getLabel());
	}
	


	@Test
	public void testGetTreeRepeated() throws Exception {
		String str = "a.a=value a\nb.a=value ba\nb.b=value bb";
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
		assertEquals("a", bNode.getLabel());
		assertEquals("value a", getValueForFilename(bNode,"filename"));
		

		node = nodes.get(1);

		assertNotNull(node);
		assertEquals("b", node.getLabel());

		bNode = node.getChildren().get(0);
		assertNotNull(bNode);
		assertEquals("a", bNode.getLabel());
		assertEquals("value ba", getValueForFilename(bNode,"filename"));

		bNode = node.getChildren().get(1);
		assertNotNull(bNode);
		assertEquals("b", bNode.getLabel());
		assertEquals("value bb", getValueForFilename(bNode,"filename"));
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

	private List<ValueNode>  getMap(String value) {
		return Arrays.asList(new ValueNode("filename", value));
	}

	private TreeNode getTestTree() {
		TreeNode aNode = new TreeNode();
		aNode.setLabel("a");
		TreeNode bNode = new TreeNode();
		bNode.setValues(getMap("b value"));
		bNode.setLabel("b");
		aNode.setChildren(new ArrayList<TreeNode>());
		aNode.getChildren().add( bNode);
		
		TreeNode cNode = new TreeNode();
		cNode.setValues(getMap("c value"));
		cNode.setLabel("c");
		aNode.getChildren().add(cNode);
		return aNode;
	}

}
