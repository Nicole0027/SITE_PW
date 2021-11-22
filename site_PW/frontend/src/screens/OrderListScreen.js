/* eslint-disable no-underscore-dangle */
import DashboardMenu from "../components/DasboardMenu";
import { deleteOrder, getMyOrders } from "../api";
import { showLoading, hideLoading, rerender, showMessage } from "../utils";

const OrderListScreen = {
  after_render: () => {
    const deleteButtons = document.getElementsByClassName("delete-button");
    Array.from(deleteButtons).forEach((deleteButton) => {
      deleteButton.addEventListener("click", async () => {
        // eslint-disable-next-line no-restricted-globals
        if (confirm("Are you sure to delete this order?")) {
          showLoading();
          const data = await deleteOrder(deleteButton.id);
          if (data.error) {
            showMessage(data.error);
          } else {
            rerender(OrderListScreen);
          }
          hideLoading();
        }
      });
    });
  },
  render: async () => {
    const orders = await getMyOrders();
    return `
    <div class="dashboard">
    ${DashboardMenu.render({ selected: "orders" })}
    <div class="dashboard-content">
      <h1>Orders</h1>
      <div class="order-list">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>DATE</th>
              <th>TOTAL</th>
              <th>PAID AT</th>
              <th>DELIVERED AT</th>
              <th class="tr-action">ACTION</th>
            <tr>
          </thead>
          <tbody>
            ${orders
              .map(
                (order) => `
            <tr>
              <td>${order._id}</td>
              <td>2021-11-19</td>
              <td>${order.totalPrice}</td>
              <td>2021-11-19</td>
              <td>${order.deliveredAt || "No"}</td>
              <td>
             
              <button id="${order._id}" class="delete-button">Delete</button>
              </td>
            </tr>
            `
              )
              .join("\n")}
          </tbody>
        </table>
      </div>
    </div>
  </div>
    `;
  },
};
export default OrderListScreen;
