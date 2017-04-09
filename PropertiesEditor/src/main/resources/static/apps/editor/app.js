(function() {
	var app, deps;

	deps = [ 'angularBootstrapNavTree' ];

	if (angular.version.full.indexOf("1.2") >= 0) {
		deps.push('ngAnimate');
	}

	app = angular.module('propertyEditor', deps);

	app.controller('PropertyEditorController', function($scope, $http) {
		var checkBranch;
		$scope.tree_event_handler = function(branch) {
			var _ref;
			
			$scope.branch = branch;

		};

		$scope.treeControl = {};
		
		
		
		
		$scope.search={_text:"",
				_match:0,
				set text (name) {
					this._text = name;
					this._match = 0;
				},
				get text () {
					return this._text;
				},
				_checkBranch:function(text,branch)
				{
					if (branch.values)
					{
						for (var i = 0, len = branch.values.length; i < len; i++) {
							var item = branch.values[i];

							var valueText = item.value;
							if(text != null && valueText != null 
								    && typeof text === 'string' && typeof valueText === 'string'
								        && text.toUpperCase() === valueText.toUpperCase())
							{
								return true;
							}
						}
					}
					return false;
				},
				_visitBranch:function(text,branch)
				{
					if(this._checkBranch(text,branch))
						return branch;
					var child = $scope.treeControl.get_first_child(branch);
					while(child)
					{
						var ret = this._visitBranch(text,child);
						if (ret)
						{
							return ret;
						}
						child = $scope.treeControl.get_next_sibling(child);
					}
					return null;
				},
				findNext:function() {
					var child = $scope.treeControl.get_first_branch();
					var branch;
					for (var i = 0; i <= this._match && child;)
					{
						branch = this._visitBranch(this._text,child);
						child = $scope.treeControl.get_next_sibling(child);

						if(branch)
						{
							i++;
						}
					}
					if(branch)
					{
						this._match++;
						$scope.treeControl.collapse_all();
						$scope.treeControl.select_branch(branch);
					}
					
				}
		};
		$scope.data = [];
		$http.get('messages/resources').then(function(response) {
			$scope.data = response.data;
		});
		$scope.save=function(){
			$http.put('messages/resources',$scope.data).then(function(response) {
				$scope.data = response.data;
			});
	    }
	});

}).call(this);