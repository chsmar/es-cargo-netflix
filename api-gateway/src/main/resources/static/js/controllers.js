angular.module('springPortfolio.controllers', ['ui.bootstrap'])
    .constant("buy", "Buy")
    .constant("sell", "Sell")
    .constant("save", "Save")
    .controller('PortfolioController',
    ['$scope', '$http', '$uibModal', 'TradeService',
    function ($scope, $http, $uibModal, tradeService) {
        $scope.notifications = [];
        $scope.positions = [];

        $scope.selectedCustomer = null;
        $scope.customers = [];
        $scope.cargos = [];

        var pushNotification = function(message) {
            $scope.notifications.unshift(message);
        };

        var validateCargo = function(cargo) {
            if (isNaN(cargo.customerId) || (cargo.customerId < 1)) {
                $scope.notifications.push("New Cargo Error: Select a customer");
                return false;
            }
            return true;
        };

        $scope.openTradeModal = function (action) {
            var modalInstance = $uibModal.open({
                templateUrl: 'tradeModal.html',
                controller: 'TradeModalController',
                size: "sm",
                resolve: {
                    action: action
                }
            });
            modalInstance.result.then(function (result) {
                var cargo = {
                    "customerId" : result.customerId,
                    "amount" : result.amount
                };
                if(validateCargo(cargo)) {
                    $http({
                        method: 'POST',
                        url: '/cargo/api/cargo',
                        data: cargo
                    }).success(function (result) {
                        console.log(result);
                    });
                }
            });
        };

        $scope.logout = function() {
            tradeService.disconnect();
        };

        $scope.refreshCargoTable = function() {
            //refrescar la tabla de cargo
            $http({
                method: 'GET',
                url: '/cargo/api/cargo',
                data: { }
            }).success(function (result) {
                console.log(result);
                $scope.positions = result;
            });
        };

        $scope.refreshCargoTable();

        tradeService.connect("/events")
            .then(function () {
                    return tradeService.listenEvents().then(null, null, function (event) {
                        console.log("TODO EVENT: ");
                        console.log(event);
                        if(event.eventType == "CargoInvoiceGeneratedEvent") {
                            //muestro en notificacion
                            pushNotification("Generated invoice Id: "+event.entityId);
                            //mostrar factura
                            var data = event.eventData;
                            var invoice = "Invoice \n CustomerId:"+ data.customerId;
                            invoice += "\n Amount: "+ data.amount;
                            invoice += "\n Date: "+ data.date;
                            alert(invoice);
                            //refrescar la tabla de cargo
                            $scope.refreshCargoTable();
                        } else {
                            alert("Error Insufficient founds");
                        }
                    });
                },
                function (error) {
                    pushNotification(error);
                });

    }])
    .controller('TradeModalController',
            ["$scope", "$uibModalInstance", "TradeService", "action","$http",
            function ($scope, $uibModalInstance, tradeService, action,$http) {

        $http({
            method: 'GET',
            url: '/customer/api/customer',
            data: { }
        }).success(function (result) {
            $scope.customers = result;
        });
        $scope.action = action;
        $scope.amount = 0;
        $scope.selectedCustomer = 0;
        $scope.trade = function () {
            $uibModalInstance.close({action: action, customerId:$scope.selectedCustomer,amount: $scope.amount});
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);