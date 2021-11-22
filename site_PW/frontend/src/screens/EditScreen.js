import { getUserInfo } from "../localStorage";
import DashboardMenu from "../components/DasboardMenu";
import { hideLoading, showLoading } from "../utils";

const EditScreen = {
  after_render: () => {
    const deleteButtons = document.getElementsByClassName("delete-button");
    Array.from(deleteButtons).forEach((deleteButton) => {
      deleteButton.addEventListener("click", async () => {
        // eslint-disable-next-line no-restricted-globals
        if (confirm("Are you sure to delete this account?")) {
          showLoading();
          document.getElementById("accounts").style.display = "none";
          hideLoading();
        }
      });
    });
  },
  render: async () => {
    const orders = await getUserInfo();
    return `
    <div class="dashboard">
    ${DashboardMenu.render({ selected: "orders" })}
    <div class="dashboard-content">
      <h1>Orders</h1>
      <div class="order-list">
        <table>
          <thead>
            <tr>
              <th>NAME</th>
              <th>EMAIL</th>
              <th>PASSWORD</th>
              
            <tr>
          </thead>
          <tbody id="accounts">
           
            <tr>
              <td>ion</td>
              <td>ion@ion</td>
              <td>ion</td>
              <td>
              <button class="delete-button">Delete Account</button>
              </td>
            </tr>
            
              
              
          </tbody>
        </table>
      </div>
    </div>
  </div>
    `;
  },
};
export default EditScreen;
