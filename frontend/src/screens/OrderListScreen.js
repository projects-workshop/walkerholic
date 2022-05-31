import React from 'react'
import { useState } from 'react'
import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Link } from 'react-router-dom'
import Error from '../components/Error'
import Loading from '../components/Loading'
import { cancelOrder, deliverOrder, getOrderList, getOrderListBySeller, getOrderListByUser } from '../_actions/OrderActions'

function OrderListScreen(props) {

    const id = props.match.params.id
    const userId = props.match.params.userId

    const [page, setPage] = useState(1)

    const auth = useSelector(state => state.auth)
    const list = useSelector(state => state.list)
    const {orders} = list

    const dispatch = useDispatch()

    const pages = [...Array(orders?.totalPage).keys()]

    const handleDeliver = (id) =>{
        dispatch(deliverOrder(id))
    }

    const handleCancel = (id) =>{
        if(window.confirm('Are you sure to cancel this order?')){
            dispatch(cancelOrder(id))
        }
    }

    useEffect(() => {
        if(id){
            dispatch(getOrderListBySeller(page, id))
        }else if(userId){
            dispatch(getOrderListByUser(page, userId))
        }else{
            dispatch(getOrderList(page))
        }
    }, [page])

    return (
        <div className="list">
            {
                list.error && list.error.message && <Error error = {list.error.message}/>
            }
            {
                list.Loading && <Loading/>
            }
            <div className="list_title">
                Orders .
            </div>

            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>User</th>
                        <th>Payment</th>
                        <th>Delivery</th>
                        <th>Status</th>
                        <th></th>
                    </tr>
                </thead>
                {
                    orders &&
                    orders.orders.map((order,index)=>(
                        <tbody key={index}>
                            <tr>
                                <td style={{textAlign:"left", paddingLeft:'10px'}}><Link to={`/order/${order.id}`}>{order.id}</Link></td>
                                <td>
                                    <div className="orderlist_user">
                                        <div className="orderlist_user_image">
                                            <img src={order.user.imageUrl} alt="" />
                                        </div>
                                        <span>{order.user.fullname}</span>
                                    </div>
                                </td>
                                <td style={{textAlign:"left", paddingLeft:'10px'}}>{order.paid? order.paidAt : "Not yet"}</td>
                                <td style={{textAlign:"left", paddingLeft:'10px'}}>{order.delivered? order.deliveredAt : "Not yet"}</td>
                                <td style={{textAlign:"left", paddingLeft:'10px'}}>{order.orderStatus}</td>
                                <td>
                                    <div className="orderlist_action">
                                        {
                                            (order.orderStatus !== "CANCEL") &&
                                            (
                                                order.delivered 
                                                ? '' 
                                                : <button style={{backgroundColor:"#edaa00"}} onClick={()=>handleDeliver(order.id)}>deliver</button>
                                                )
                                        }
                                        {
                                            order.orderStatus ==="CANCEL"
                                            ? ''
                                            : <button style={{backgroundColor:"#bd4720"}} onClick={()=>handleCancel(order.id)}>cancel</button>
                                        }
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    ))
                }
            </table>

            {
                // orders.totalPage > 1 &&
                <nav aria-label="Pagination">
                    <ul className="pagination">
                        <li className="page-item" style={{borderRadius:"10px 0px 0px 10px"}}>
                            <i className="fas fa-backward" onClick={()=>setPage(1)}></i>
                        </li>
                        {
                            pages.map((x, index)=>(
                                <li key={index} className={`page-item ${page===x+1 && 'page_active'}`} onClick={()=>setPage(x+1)}>{x+1}</li>
                            ))
                        }
                        <li className="page-item" style={{borderRadius:"0px 10px 10px 0px"}}>
                            <i className="fas fa-forward"></i>
                        </li>
                    </ul>
                </nav>
            }
        </div>
    )
}

export default OrderListScreen
