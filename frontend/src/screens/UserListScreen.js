import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { deleteUser, getUserList } from '../_actions/UserActions'
import {levelIconShow} from '../utils/MediaShow'
import Error from '../components/Error'
import Loading from '../components/Loading'

function UserListScreen() {

    const [page, setPage] = useState(1)
    const [sort, setSort] = useState('id')

    const list = useSelector(state => state.list)
    const {users} = list

    const pages = [...Array(users?.totalPage).keys()]

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(getUserList(page, sort))
    }, [dispatch, page, sort])

    const handleDelete = (id)=>{
        if(window.confirm(`Are you sure to delete user${id}?`)){
            dispatch(deleteUser(id))
        }
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
                Users .
            </div>

            <table className="userlist">
                <thead>
                    <th ><span onClick={()=>setSort('id')}>ID</span></th>
                    <th >Image</th>
                    <th ><span onClick={()=>setSort('firstname')}>FirstName</span></th>
                    <th ><span onClick={()=>setSort('lastname')}>LastName</span></th>
                    <th ><span onClick={()=>setSort('email')}>Email</span></th>
                    <th >Role</th>
                    <th >Level</th>
                    <th></th>
                </thead>
                {
                    users &&
                    users.users.map((user,index)=>(
                        <tbody key={index}>
                            <td >{user.id}</td>
                            <td>
                                <div className="userlist_image">
                                    <img src={user.imageUrl} alt="" />
                                </div>
                            </td>
                            <td style={{textAlign:"left", paddingLeft:'10px'}}>{user.firstname}</td>
                            <td style={{textAlign:"left", paddingLeft:'10px'}}>{user.lastname}</td>
                            <td style={{textAlign:"left", paddingLeft:'10px'}}>{user.email}</td>
                            <td>{user.role}</td>
                            <td>{levelIconShow(user.level)}</td>
                            <td>
                                <div className="orderlist_action">
                                    <i class="far fa-trash-alt" style={{cursor:"pointer"}} onClick={()=>handleDelete(user.id)}></i>
                                </div>
                            </td>
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
                                <li className={`page-item ${page===x+1 && 'page_active'}`} onClick={()=>setPage(x+1)}>{x+1}</li>
                            ))
                        }
                        <li className="page-item" style={{borderRadius:"0px 10px 10px 0px"}}>
                            <i className="fas fa-forward" onClick={()=>setPage(users.totalPage)}></i>
                        </li>
                    </ul>
                </nav>
            }
        </div>
    )
}

export default UserListScreen
