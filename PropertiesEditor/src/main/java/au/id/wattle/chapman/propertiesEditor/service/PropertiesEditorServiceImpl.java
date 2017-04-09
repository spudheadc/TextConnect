package au.id.wattle.chapman.propertiesEditor.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import lombok.Setter;

import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

import au.id.wattle.chapman.propertiesEditor.model.TreeNode;

public class PropertiesEditorServiceImpl implements PropertyEditorService {

	@Setter
	private List<Resource> files;

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.id.wattle.chapman.propertiesEditor.service.PropertyEditorService#
	 * getTree (java.lang.String)
	 */
	@Override
	public List<TreeNode> getTree() {
		ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
		for (Iterator<Resource> fileIterator = files.iterator(); fileIterator.hasNext();) {
			Resource resource = fileIterator.next();
			Properties prop = new Properties();
			try {
				prop.load(resource.getInputStream());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			Set<String> keys = new TreeSet<String>(prop.stringPropertyNames());
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String key = iterator.next();
				addNode(nodes, key, prop.getProperty(key), resource.getFilename());
			}

		}

		return nodes;
	}

	private TreeNode addNode(List<TreeNode> nodes, String key, String value, String filename) {
		String[] words = key.split("\\.");
		TreeNode tree = getRootNode(nodes, words[0]);

		for (int i = 1; i < words.length; i++) {
			TreeNode child = findChild(tree, words[i]);
			if (child != null)
				tree = child;
			else {
				child = new TreeNode();
				child.setLabel(words[i]);

				if (tree.getChildren() == null)
					tree.setChildren(new ArrayList<TreeNode>());
				tree.getChildren().add(child);

				tree = child;
			}
		}
		Map<String, String> map = new TreeMap<String, String>();
		map.put(filename, value);
		tree.setValue(map);
		return tree;

	}

	private TreeNode getRootNode(List<TreeNode> nodes, String rootLabel) {
		TreeNode tree = null;
		for (Iterator<TreeNode> iterator = nodes.iterator(); iterator.hasNext();) {
			TreeNode treeNode = iterator.next();

			if (rootLabel.equalsIgnoreCase(treeNode.getLabel())) {
				tree = treeNode;
				break;
			}
		}
		if (tree == null) {
			tree = new TreeNode();
			tree.setLabel(rootLabel);
			nodes.add(tree);
		}
		return tree;
	}

	private TreeNode findChild(TreeNode tree, String name) {
		if (tree == null || tree.getChildren() == null)
			return null;

		List<TreeNode> list = tree.getChildren();

		for (Iterator<TreeNode> iterator = list.iterator(); iterator.hasNext();) {
			TreeNode node = iterator.next();
			if (node.getLabel().equalsIgnoreCase(name))
				return node;

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.id.wattle.chapman.propertiesEditor.service.PropertyEditorService#
	 * setTree (java.lang.String,
	 * au.id.wattle.chapman.propertiesEditor.model.TreeNode)
	 */
	@Override
	public List<TreeNode> setTree(List<TreeNode> tree) {
		

		for (Iterator<Resource> fileIterator = files.iterator(); fileIterator.hasNext();) {
			Resource resource = fileIterator.next();

			Properties prop = new Properties();
			for (Iterator<TreeNode> iterator = tree.iterator(); iterator.hasNext();) {
				TreeNode treeNode = iterator.next();
				populateProperties(prop, treeNode, null, resource.getFilename());

			}

			try {
				assert (WritableResource.class.isInstance(resource));
				prop.store(((WritableResource)resource).getOutputStream(), null);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		return tree;

	}

	private void populateProperties(Properties prop, TreeNode node, String parentName, String filename) {
		String name = node.getLabel();
		if (parentName != null)
			name = parentName + "." + node.getLabel();
		String value = null;
		if (node.getValue() != null)
			value = (String) node.getValue().get(filename);
		if (value != null) {
			prop.setProperty(name, value);
		} else {
			if (node.getChildren() != null) {
				for (Iterator<TreeNode> iterator = node.getChildren().iterator(); iterator.hasNext();) {
					TreeNode child = iterator.next();
					populateProperties(prop, child, name, filename);
				}
			}
		}
	}

}
