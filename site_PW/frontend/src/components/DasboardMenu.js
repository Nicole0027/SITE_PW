/* eslint-disable arrow-body-style */
const DashboardMenu = {
  render: (props) => {
    return `
        <div class="dashboard-menu">
            <ul>
                <li class="${props.selected === "dashboard" ? "selected" : ""}">
                    <a href="/#/dashboard">Dashboard</a>
                </li>

                <li class="${props.selected === "orders" ? "selected" : ""}">
                    <a href="/#/orderlist">Orders</a>
                 </li>

                 <li class="${props.selected === "edit" ? "selected" : ""}">
                 <a href="/#/edit">Accounts</a>
              </li>

            </ul>
        </div>
        `;
  },
};

export default DashboardMenu;
