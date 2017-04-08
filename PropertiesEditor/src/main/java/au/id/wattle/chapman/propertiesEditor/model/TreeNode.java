package au.id.wattle.chapman.propertiesEditor.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class TreeNode {

	private String label;
	private Map value;
	@JsonBackReference("children")
	private TreeNode parent;

	@JsonManagedReference("children")
	private List<TreeNode> children = null;

}
