package au.id.wattle.chapman.propertiesEditor.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@JsonInclude(Include.NON_EMPTY)
public class TreeNode {

    @ApiModelProperty(notes = "The label that should be shown on each node of the tree.")
	private String label;
    
    @ApiModelProperty(notes = "A map of values for a leaf node. The map will have a key which is the "
    		+ "filename which contains the node, and a value which is the value in that file.")
	private Map<String, String> value;


    @ApiModelProperty(notes = "The children of this node.")
	@JsonManagedReference("children")
	private List<TreeNode> children = null;

}
