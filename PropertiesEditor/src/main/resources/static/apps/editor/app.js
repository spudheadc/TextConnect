(function() {
	var app, deps;

	deps = [ 'angularBootstrapNavTree' ];

	if (angular.version.full.indexOf("1.2") >= 0) {
		deps.push('ngAnimate');
	}

	app = angular.module('propertyEditor', deps);

	app.controller('PropertyEditorController', function($scope, $http) {
		$scope.tree_event_handler = function(branch) {
			var _ref;
			
			$scope.branch = branch;
			$scope.items = [];
			angular.forEach(branch.value, function(value, key) {
				this.push({
					key : key,
					value : value
				});
			}, $scope.items);

		};
		$scope.data = [];
		$http.get('editableProperties').then(function(response) {
			$scope.data = response.data;
		});
		$scope.save=function(){
			$http.put('editableProperties',$scope.data).then(function(response) {
				$scope.data = response.data;
			});
	    }
	});

}).call(this);