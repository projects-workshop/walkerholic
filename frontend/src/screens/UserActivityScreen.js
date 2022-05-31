import React from 'react'
import { useEffect } from 'react'
import { useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import EditUserActivity from '../components/activity/EditUserActivity'
import UserActivityCard from '../components/activity/UserActivityCard'
import Error from '../components/Error'
import Loading from '../components/Loading'
import { levelDescriptionIconShow } from '../utils/MediaShow'
import { getUserActivities } from '../_actions/ActivityActions'

function UserActivityScreen(props) {

    const [page, setPage] = useState(1)
    const [isAdd, setIsAdd] = useState(false)
    const id = props.match.params.userId

    const auth = useSelector(state => state.auth)
    const activity = useSelector(state => state.activity)

    const pages = [...Array(activity.userActivity?.totalPage).keys()]


    const dispatch = useDispatch()

    useEffect(() => {
        if(auth.user && id) {
            dispatch(getUserActivities(page))
        }
    }, [page, id, auth.user])


    return (
        <div className="useractivity">
            {
                activity.error && activity.error.message && <Error error = {activity.error.message}/>
            }
            {
                activity.Loading && <Loading/>
            }

            {
                auth.user &&
                <div className="useractivity_title">
                    {auth.user.firstname}'s Activities
                    {
                        levelDescriptionIconShow(auth.user.level)
                    }
                </div>
            }

            <div style={{textAlign:"right", margin:"3rem 2rem 3rem 0"}}>
                <div className="useractivity_score">Current Score : <span>{activity.userActivity?.score}</span></div>
                <button onClick={()=>setIsAdd(true)}>Add Activity</button>
            </div>

            <div className="useractivity_card_container">
                {
                    activity.userActivity?.activities.map((activity,index)=>(
                        <UserActivityCard activity = {activity} key={index} />
                    ))
                }
            </div>

            {
                isAdd &&
                <EditUserActivity setIsEdit={setIsAdd} isEdit={isAdd} />
            }


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
                            <i className="fas fa-forward" onClick={()=>setPage(activity.userActivity.totalPage)}></i>
                        </li>
                    </ul>
                </nav>
            }
        </div>
    )
}

export default UserActivityScreen
