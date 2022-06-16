import axios from "axios"
import { ADD_TO_CART_FAIL, ADD_TO_CART_REQUEST, ADD_TO_CART_SUCCESS, CANCEL_ORDER_FAIL, CANCEL_ORDER_REQUEST, CANCEL_ORDER_SUCCESS, CREATE_CART_FAIL, CREATE_CART_REQUEST, CREATE_CART_SUCCESS, CREATE_ORDER_FAIL, CREATE_ORDER_REQUEST, CREATE_ORDER_SUCCESS, DELETE_CARTITEM_FAIL, DELETE_CARTITEM_REQUEST, DELETE_CARTITEM_SUCCESS, DELETE_ORDERITEM_FAIL, DELETE_ORDERITEM_REQUEST, DELETE_ORDERITEM_SUCCESS, DELIVER_ORDER_FAIL, DELIVER_ORDER_REQUEST, DELIVER_ORDER_SUCCESS, GET_CARTITEMS_FAIL, GET_CARTITEMS_REQUEST, GET_CARTITEMS_SUCCESS, GET_ORDER_FAIL, GET_ORDER_LIST_FAIL, GET_ORDER_LIST_REQUEST, GET_ORDER_LIST_SUCCESS, GET_ORDER_REQUEST, GET_ORDER_SUCCESS, UPDATE_ORDERITEM_QTY_FAIL, UPDATE_ORDERITEM_QTY_REQUEST, UPDATE_ORDERITEM_QTY_SUCCESS } from "../_constants/OrderConstants"

export const getCart = (id) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:GET_CARTITEMS_REQUEST
    })

    try{
        const res = await axios.get(`/carts?userId=${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_CARTITEMS_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:GET_CARTITEMS_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}

export const createCart = () =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()


    dispatch({
        type:CREATE_CART_REQUEST
    })

    try{
        const res = await axios.post(`/carts?userId=${user.id}`,null,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:CREATE_CART_SUCCESS,
            payload:res.data
        })

        return res.data
    }catch(error){
        dispatch({
            type:CREATE_CART_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}

export const addCart = (qty, productId, cartId) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()
    const {cart : {items}} = getState()

    var cartItemRequest={};
    
    dispatch({
        type:ADD_TO_CART_REQUEST
    })

    try{
        let res;
        if(items && items.filter(i=>i.productId===productId).length>0){
            const existItem = items.filter(i=>i.productId===productId)

            res = await axios.put(`/cart-items/${existItem[0].id}?qty=${existItem[0].qty+qty}`, null,{
                headers : {Authorization : `Bearer ${token}`}
            })
    
        }else{
            cartItemRequest={
                qty:qty,
                productId:productId,
                cartId:cartId
                }
            res = await axios.post(`/cart-items`, cartItemRequest,{
                headers : {Authorization : `Bearer ${token}`}
            })
        
        }

        dispatch({
            type:ADD_TO_CART_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:ADD_TO_CART_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}


export const updateQty = (id, qty) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:UPDATE_ORDERITEM_QTY_REQUEST
    })


    try{
        await axios.put(`/cart-items/${id}?qty=${qty}`,null,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:UPDATE_ORDERITEM_QTY_SUCCESS,
            payload:{
                id:id,
                qty:qty
            }
        })


    }catch(error){
        dispatch({
            type:UPDATE_ORDERITEM_QTY_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}


export const deleteCartItem = (id) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:DELETE_CARTITEM_REQUEST
    })


    try{
        await axios.delete(`/cart-items/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:DELETE_CARTITEM_SUCCESS,
            payload:id
        })


    }catch(error){
        dispatch({
            type:DELETE_CARTITEM_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}



export const deleteOrderItem = (id) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:DELETE_ORDERITEM_REQUEST
    })


    try{
        await axios.delete(`/order-items/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:DELETE_ORDERITEM_SUCCESS,
            payload:id
        })


    }catch(error){
        dispatch({
            type:DELETE_ORDERITEM_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}



export const getOrderList = (page) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:GET_ORDER_LIST_REQUEST
    })


    try{

        const res = await axios.get(`/orders`,{
            params:{
                // pageRequest : {
                    page,
                    sort : '',
                // }
            }
        },{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_ORDER_LIST_SUCCESS,
            payload: res.data
        })


    }catch(error){
        dispatch({
            type:GET_ORDER_LIST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}

export const getOrderListBySeller = (page,id) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:GET_ORDER_LIST_REQUEST
    })


    try{
        // const res = await axios.get(`/users/${id}/orders/seller?page=${page}`,{
        //     headers : {Authorization : `Bearer ${token}`}
        // })

        const res = await axios.get(`/orders`,{
            params:{
                // pageRequest : {
                    page,
                    sort : '',
                // },
                sellerId : id
            }
        },{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_ORDER_LIST_SUCCESS,
            payload: res.data
        })


    }catch(error){
        dispatch({
            type:GET_ORDER_LIST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}

export const getOrderListByUser = (page,userId) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:GET_ORDER_LIST_REQUEST
    })


    try{
        // const res = await axios.get(`/users/${userId}/orders?page=${page}`,{
        //     headers : {Authorization : `Bearer ${token}`}
        // })
        const res = await axios.get(`/orders`,{
            params:{
                // pageRequest : {
                    page,
                    sort : '',
                // },
                userId : userId,
            }
        },{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_ORDER_LIST_SUCCESS,
            payload: res.data
        })


    }catch(error){
        dispatch({
            type:GET_ORDER_LIST_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}

// export const createOrder = (id, orderCreateDTO) =>async(dispatch, getState)=>{

//     const {auth : {user}} = getState()
//     const {auth : {token}} = getState()

//     dispatch({
//         type:CREATE_ORDER_REQUEST
//     })


//     try{
//         await axios.post(`/orders/${id}/payment`, orderCreateDTO,{
//             headers : {Authorization : `Bearer ${token}`}
//         })
//         dispatch({
//             type:CREATE_ORDER_SUCCESS
//         })


//     }catch(error){
//         dispatch({
//             type:CREATE_ORDER_FAIL,
//             payload: error.response && error.response.data
//             ? error.response.data
//             : error.message            
//         })
//         // console.log(error)
//     }
// }

export const createOrder = (orderRequest) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:CREATE_ORDER_REQUEST
    })


    try{
        const res = await axios.post(`/orders`, orderRequest,{
            headers : {Authorization : `Bearer ${token}`}
        })
        dispatch({
            type:CREATE_ORDER_SUCCESS
        })
        return res.data.id


    }catch(error){
        dispatch({
            type:CREATE_ORDER_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        // console.log(error)
    }
}

export const getOrder = (id) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:GET_ORDER_REQUEST
    })

    // console.log(token)
    // console.log(id)

    try{
        const res = await axios.get(`/orders/${id}`,{
            headers : {Authorization : `Bearer ${token}`}
        })

        dispatch({
            type:GET_ORDER_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:GET_ORDER_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}
export const cancelOrder = (id, transactionId) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:CANCEL_ORDER_REQUEST
    })


    let res = {}
    try{

        await axios.get('/paypal',{
            headers : {Authorization : `Bearer ${token}`}
        }).then( async(rest) =>{
            const paypalToken = `${rest.data.clientId}:${rest.data.clientSecret}`;
            const encodedToken = btoa(paypalToken)
            await axios.post(`https://api.sandbox.paypal.com/v2/payments/captures/${transactionId}/refund`,null,{
                headers : {
                Authorization: "Basic " + encodedToken,
                'Content-type':'application/json'
            }}).then(async(result) =>{
                res = await axios.put(`/orders/${id}/cancellation`,{
                    headers : {Authorization : `Bearer ${token}`}
                })
            })
    
        })

        dispatch({
            type:CANCEL_ORDER_SUCCESS,
            payload:res.data
        })


    }catch(error){
        dispatch({
            type:CANCEL_ORDER_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
    }
}

export const deliverOrder = (id) =>async(dispatch, getState)=>{

    const {auth : {user}} = getState()
    const {auth : {token}} = getState()

    dispatch({
        type:DELIVER_ORDER_REQUEST
    })


    try{
        const res = await axios.put(`/orders/${id}/delivery`,{
            headers : {Authorization : `Bearer ${token}`}
        })
        dispatch({
            type:DELIVER_ORDER_SUCCESS,
            payload:res.data
        })
        console.log(res.data)


    }catch(error){
        dispatch({
            type:DELIVER_ORDER_FAIL,
            payload: error.response && error.response.data
            ? error.response.data
            : error.message            
        })
        console.log(error)
    }
}
