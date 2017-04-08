package au.id.wattle.chapman.propertiesEditor.service;

import java.util.List;

import au.id.wattle.chapman.propertiesEditor.model.TreeNode;

public interface PropertyEditorService {

	public abstract List<TreeNode> getTree();

	public abstract List<TreeNode> setTree(List<TreeNode> tree);

}