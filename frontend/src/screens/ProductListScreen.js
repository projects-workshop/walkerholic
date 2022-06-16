import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { deleteProduct, getProductList, getSellerProductList } from '../_actions/ProductActions'
import Rating from '../components/product/Rating'
import EditProduct from '../components/product/EditProduct'
import Error from '../components/Error'
import Loading from '../components/Loading'

function ProductListScreen(props) {

    const sellerId = props.match.params.sellerId

    const [page, setPage] = useState(1)
    const [sort, setSort] = useState('id')

    const [isEdit, setIsEdit] = useState(false)
    const [editProduct, setEditProduct] = useState(null)
    const [isCreate, setIsCreate] = useState(false)

    const list = useSelector(state => state.list)
    const {products} = list

    const pages = [...Array(products?.totalPage).keys()]

    const dispatch = useDispatch()

    useEffect(() => {
        if(sellerId){
            dispatch(getSellerProductList(page, sort, sellerId))
        }else{
            dispatch(getProductList(page, sort))
        }
    }, [sellerId,page, sort])

    const handleDelete = (id)=>{
        if(window.confirm(`Are you sure to delete product${id}?`)){
            dispatch(deleteProduct(id))
        }
    }

    const handleEdit = (product)=>{
        setEditProduct(product)
        setIsEdit(true)
    }

    return (
        <div className="list">
            {
                list.error && list.error.message && <Error error = {list.error.message}/>
            }
            {
                list.Loading && <Loading/>
            }
            
            <div className="list_title">
                Products .
            </div>

            <div style={{textAlign:"right", marginBottom:"1rem"}}>
                <button onClick={()=>setIsCreate(true)}>Create Product</button>
            </div>

            <table className="userlist">
                <thead>
                    <tr>
                        <th ><span onClick={()=>setSort('id')}>ID</span></th>
                        <th >Image</th>
                        <th ><span onClick={()=>setSort('name')}>Name</span></th>
                        <th >Stock</th>
                        <th ><span onClick={()=>setSort('price')}>Price</span></th>
                        <th ><span onClick={()=>setSort('average')}>Average</span></th>
                        <th></th>
                    </tr>
                </thead>
                {
                    products &&
                    products.products.map((product,index)=>(
                        <tbody key={index}>
                            <tr>
                                <td >{product.id}</td>
                                <td>
                                    <div className="userlist_image">
                                        <img src={product.imagesUrl[0]} alt="" />
                                    </div>
                                </td>
                                <td style={{textAlign:"left", paddingLeft:'10px'}}>{product.name}</td>
                                <td >{product.stock}</td>
                                <td >{product.price}</td>
                                <td><Rating rating={product.average}/></td>
                                <td>
                                    <div className="orderlist_action">
                                        <i className="far fa-trash-alt" style={{cursor:"pointer"}} onClick={()=>handleDelete(product.id)}></i>
                                        <i className="far fa-edit" onClick={()=> handleEdit(product)}></i>
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
                            <i className="fas fa-forward" onClick={()=>setPage(products.totalPage)}></i>
                        </li>
                    </ul>
                </nav>
            }

            {
                isEdit &&
                <EditProduct product={editProduct} setIsEdit={setIsEdit} isEdit={isEdit}/> 
            }

            {
                isCreate &&
                <EditProduct setIsEdit={setIsCreate} isEdit={isCreate}/>
            }
        </div>
    )
}

export default ProductListScreen
